package com.udwall.firewall.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.udwall.firewall.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val startOnBoot by viewModel.startOnBoot.collectAsState()
    val blockAllMode by viewModel.blockAllMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackgroundStart, DarkBackgroundEnd)))
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Settings", fontWeight = FontWeight.Black, color = TextPrimary) },
            actions = {},
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "GENERAL SECURITY",
                color = AccentPink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            SettingsGlassCard {
                SettingsToggleItem(
                    icon = Icons.Rounded.PowerSettingsNew,
                    title = "Start on Boot",
                    description = "Auto-start firewall when device restarts",
                    checked = startOnBoot,
                    onCheckedChange = { viewModel.setStartOnBoot(it) }
                )
                HorizontalDivider(color = GlassBorder.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))
                SettingsToggleItem(
                    icon = Icons.Rounded.GppMaybe,
                    title = "Strict Mode",
                    description = "Block ALL apps regardless of whitelist",
                    checked = blockAllMode,
                    onCheckedChange = { viewModel.setBlockAllMode(it) }
                )
            }

            Text(
                "NOTIFICATIONS",
                color = AccentPurple,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            SettingsGlassCard {
                SettingsToggleItem(
                    icon = Icons.Rounded.NotificationsActive,
                    title = "Protection Status",
                    description = "Show permanent notification when active",
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                )
            }

            Text(
                "ABOUT",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            SettingsGlassCard {
                SettingsInfoItem(Icons.Rounded.Info, "Version", "1.0.0 (Hardened Build)")
                SettingsInfoItem(Icons.Rounded.Security, "Encryption", "AES-256 SQLCipher Active")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "UDWALL: Ultimate Defense Wall. Zero Trust. Zero Leaks.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = TextSecondary.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun SettingsGlassCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(LiquidGlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = TextPrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(description, color = TextSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonGreen,
                checkedTrackColor = NeonGreen.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SettingsInfoItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(16.dp))
        Text(title, color = TextPrimary, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(value, color = TextSecondary, fontSize = 14.sp)
    }
}
