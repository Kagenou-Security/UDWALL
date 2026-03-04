package com.udwall.firewall

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.net.VpnService
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.udwall.firewall.service.UdwallVpnService
import com.udwall.firewall.ui.dashboard.DashboardScreen
import com.udwall.firewall.ui.apps.AppsScreen
import com.udwall.firewall.ui.logs.LogsScreen
import com.udwall.firewall.ui.settings.SettingsScreen
import com.udwall.firewall.ui.theme.UDWALLTheme
import com.udwall.firewall.ui.theme.TextPrimary
import com.udwall.firewall.ui.theme.TextSecondary
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val vpnPrepareLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            startUdwallVpn()
        }
    }

    private val exitReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == UdwallVpnService.ACTION_EXIT_APP) {
                closeAppFully()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        val filter = IntentFilter(UdwallVpnService.ACTION_EXIT_APP)
        ContextCompat.registerReceiver(
            this,
            exitReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        requestBatteryOptimizations()

        setContent {
            UDWALLTheme {
                val navController = rememberNavController()
                val items = listOf(
                    Screen.Dashboard,
                    Screen.Apps,
                    Screen.Logs,
                    Screen.Settings
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary,
                            tonalElevation = 0.dp
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = TextPrimary,
                                        unselectedIconColor = TextSecondary,
                                        indicatorColor = Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController, 
                        startDestination = Screen.Dashboard.route, 
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Dashboard.route) {
                            val isVpnRunning by UdwallVpnService.isVpnRunning.collectAsState()
                            DashboardScreen(
                                isFirewallEnabled = isVpnRunning,
                                onToggleFirewall = { enable -> toggleFirewall(enable) }
                            )
                        }
                        composable(Screen.Apps.route) {
                            AppsScreen()
                        }
                        composable(Screen.Logs.route) {
                            LogsScreen()
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }

    private fun closeAppFully() {
        finishAffinity()
        exitProcess(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(exitReceiver)
        } catch (_: Exception) {
            // Already unregistered
        }
    }

    private fun toggleFirewall(enable: Boolean) {
        if (enable) {
            showVpnDisclosure()
        } else {
            authenticateAndStop()
        }
    }

    private fun showVpnDisclosure() {
        AlertDialog.Builder(this)
            .setTitle("Privacy & Security Disclosure")
            .setMessage("UDWALL uses a local VPN service to filter network traffic on your device.\n\n" +
                    "• It does NOT route traffic through external servers.\n" +
                    "• It does NOT hide your IP address or encrypt your traffic over the internet.\n" +
                    "• All filtering happens strictly offline on your device.\n\n" +
                    "This is required for the firewall to block unauthorized app connections.")
            .setPositiveButton("I Understand") { _, _ ->
                val intent = VpnService.prepare(this)
                if (intent != null) {
                    vpnPrepareLauncher.launch(intent)
                } else {
                    startUdwallVpn()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun authenticateAndStop() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    stopUdwallVpn()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(applicationContext, "Authentication failed: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Security Change")
            .setSubtitle("Authenticate to disable UDWALL firewall")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun startUdwallVpn() {
        val startIntent = Intent(this, UdwallVpnService::class.java)
        startService(startIntent)
    }

    private fun stopUdwallVpn() {
        val stopIntent = Intent(this, UdwallVpnService::class.java).apply {
            action = UdwallVpnService.ACTION_STOP_VPN
        }
        startService(stopIntent)
    }

    private fun requestBatteryOptimizations() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = "package:$packageName".toUri()
                }
                startActivity(intent)
            } catch (_: Exception) {
                // Some devices might not support this intent directly
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Rounded.Dashboard)
    data object Apps : Screen("apps", "Apps", Icons.AutoMirrored.Rounded.List)
    data object Logs : Screen("logs", "Logs", Icons.Rounded.History)
    data object Settings : Screen("settings", "Settings", Icons.Rounded.Settings)
}
