package com.udwall.firewall.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udwall.firewall.data.SettingsRepository
import com.udwall.firewall.service.UdwallVpnService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    val startOnBoot = settingsRepository.startOnBoot.first()
                    if (startOnBoot) {
                        val startIntent = Intent(context, UdwallVpnService::class.java)
                        context.startForegroundService(startIntent)
                    }
                } catch (e: Exception) {
                    // Log error or ignore
                }
            }
        }
    }
}
