package com.udwall.firewall.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udwall.firewall.MainActivity
import com.udwall.firewall.data.AppDao
import com.udwall.firewall.data.LogDao
import com.udwall.firewall.data.LogEntity
import com.udwall.firewall.data.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.FileInputStream
import java.net.InetAddress
import java.nio.ByteBuffer
import javax.inject.Inject

@AndroidEntryPoint
class UdwallVpnService : VpnService() {

    @Inject lateinit var appDao: AppDao
    @Inject lateinit var logDao: LogDao
    @Inject lateinit var settingsRepository: SettingsRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var vpnInterface: ParcelFileDescriptor? = null
    private var observeJob: Job? = null
    
    private val _isGlobalBlockEnabled = MutableStateFlow(false)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_VPN) {
            stopFirewall()
        } else {
            startFirewall()
        }
        return START_STICKY
    }

    private fun startFirewall() {
        _isVpnRunning.value = true
        
        // Observe Settings & Whitelist
        observeJob?.cancel()
        observeJob = serviceScope.launch {
            combine(
                appDao.getWhitelistedApps(),
                settingsRepository.blockAllMode,
                settingsRepository.notificationsEnabled
            ) { whitelisted, blockAll, notify ->
                Triple(whitelisted, blockAll, notify)
            }.collectLatest { (whitelisted, blockAll, notify) ->
                _isGlobalBlockEnabled.value = blockAll
                updateNotification(notify, blockAll)
                
                val packages = if (blockAll) emptySet() else whitelisted.map { it.packageName }.toSet()
                setupVpn(packages)
            }
        }

        serviceScope.launch {
            startPacketSink()
        }
    }

    private fun updateNotification(enabled: Boolean, isStrict: Boolean) {
        if (!enabled) {
            stopForeground(STOP_FOREGROUND_DETACH)
            return
        }
        val text = if (isStrict) "Strict Mode: ALL Traffic Blocked" else "Protection Active (Whitelist Mode)"
        startForeground(NOTIFICATION_ID, createNotification(text))
    }

    @Synchronized
    private fun setupVpn(whitelistedPackages: Set<String>) {
        try {
            val builder = Builder()
            builder.addAddress("10.0.0.2", 32)
            builder.addAddress("fd00:1:fd00:1:fd00:1:fd00:1", 128)
            builder.addRoute("0.0.0.0", 0)
            builder.addRoute("::", 0)
            builder.addDnsServer("1.1.1.1") 
            builder.setSession("UDWALL Firewall")
            builder.setMtu(1500)

            // Only apps that bypass the VPN get internet
            whitelistedPackages.forEach { pkg ->
                try {
                    builder.addDisallowedApplication(pkg)
                } catch (e: Exception) {
                    Log.w(TAG, "Bypass fail for $pkg")
                }
            }

            val oldInterface = vpnInterface
            vpnInterface = builder.establish()
            oldInterface?.close()
        } catch (e: Exception) {
            Log.e(TAG, "VPN Establishment failed", e)
        }
    }

    private fun startPacketSink() {
        val packet = ByteBuffer.allocate(32767)
        while (_isVpnRunning.value) {
            val pfd = synchronized(this) { vpnInterface }
            if (pfd == null) {
                Thread.sleep(100)
                continue
            }
            
            try {
                val inputStream = FileInputStream(pfd.fileDescriptor)
                val length = inputStream.read(packet.array())
                if (length > 0) {
                    // Log the blocked packet
                    parseAndLogPacket(packet, length)
                    packet.clear()
                }
            } catch (e: Exception) {
                if (_isVpnRunning.value) Thread.sleep(100)
            }
        }
    }

    private fun parseAndLogPacket(buffer: ByteBuffer, length: Int) {
        try {
            val version = (buffer.get(0).toInt() shr 4) and 0x0F
            val protocol: String
            val destIp: String
            var destPort = 0

            if (version == 4) {
                protocol = when (buffer.get(9).toInt()) {
                    6 -> "TCP"
                    17 -> "UDP"
                    1 -> "ICMP"
                    else -> "IPv4"
                }
                val ipBytes = ByteArray(4)
                buffer.position(16)
                buffer.get(ipBytes)
                destIp = InetAddress.getByAddress(ipBytes).hostAddress ?: "Unknown"
                
                if (protocol == "TCP" || protocol == "UDP") {
                    val ihl = (buffer.get(0).toInt() and 0x0F) * 4
                    destPort = ((buffer.get(ihl + 2).toInt() and 0xFF) shl 8) or (buffer.get(ihl + 3).toInt() and 0xFF)
                }
            } else {
                destIp = "IPv6 Traffic"
                protocol = "IPv6"
            }

            serviceScope.launch {
                logDao.insertLog(LogEntity(
                    timestamp = System.currentTimeMillis(),
                    packageName = "Trapped", // We don't know the exact app without deeper inspection
                    appName = "Blocked Connection",
                    destinationIp = destIp,
                    destinationPort = destPort,
                    protocol = protocol,
                    isBlocked = true
                ))
            }
        } catch (e: Exception) {
            // Silently fail if packet parsing fails
        }
    }

    private fun stopFirewall() {
        _isVpnRunning.value = false
        observeJob?.cancel()
        synchronized(this) {
            vpnInterface?.close()
            vpnInterface = null
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        
        // Broadcast to close the activity if it's open
        val broadcastIntent = Intent(ACTION_EXIT_APP)
        sendBroadcast(broadcastIntent)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "UDWALL Status", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    private fun createNotification(content: String): android.app.Notification {
        val stopIntent = Intent(this, UdwallVpnService::class.java).apply {
            action = ACTION_STOP_VPN
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("UDWALL Protection")
            .setContentText(content)
            .setSmallIcon(com.udwall.firewall.R.mipmap.ic_launcher)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "STOP FIREWALL", stopPendingIntent)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            .build()
    }

    override fun onDestroy() {
        stopFirewall()
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "UdwallVpnService"
        private const val CHANNEL_ID = "udwall_v2_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_STOP_VPN = "com.udwall.firewall.STOP_VPN"
        const val ACTION_EXIT_APP = "com.udwall.firewall.EXIT_APP"

        private val _isVpnRunning = MutableStateFlow(false)
        val isVpnRunning = _isVpnRunning.asStateFlow()
    }
}
