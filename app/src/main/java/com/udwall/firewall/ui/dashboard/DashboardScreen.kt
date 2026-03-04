package com.udwall.firewall.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.udwall.firewall.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    isFirewallEnabled: Boolean,
    onToggleFirewall: (Boolean) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val blockedToday by viewModel.blockedTodayCount.collectAsState()
    val whitelisted by viewModel.whitelistedCount.collectAsState()
    val isStrict by viewModel.isStrictMode.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "BgAnim")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(DarkBackgroundStart, DarkBackgroundEnd, AccentPurple.copy(alpha = 0.3f)),
                    start = Offset(animOffset, 0f),
                    end = Offset(animOffset + 500f, 1500f)
                )
            )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text("UDWALL", fontWeight = FontWeight.Black, fontSize = 28.sp, color = TextPrimary) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    LiquidOrbToggle(
                        isActive = isFirewallEnabled,
                        onToggle = { onToggleFirewall(!isFirewallEnabled) }
                    )
                    
                    Spacer(modifier = Modifier.height(64.dp))
                    
                    GlassStatsPanel(
                        isStrict = isStrict,
                        blockedCount = blockedToday,
                        whitelistedCount = whitelisted
                    )
                }

                // Watermark "made by udrit"
                Text(
                    text = "made by udrit",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .alpha(0.3f),
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun LiquidOrbToggle(isActive: Boolean, onToggle: () -> Unit) {
    val infinite = rememberInfiniteTransition(label = "Orb")
    val glowScale by infinite.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    val activeColor = if (isActive) NeonGreen else NeonRed
    val baseColor = if (isActive) LiquidGlassActive else LiquidGlassInactive

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(280.dp)) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer { scaleX = glowScale; scaleY = glowScale }
                .background(Brush.radialGradient(listOf(activeColor.copy(alpha = 0.2f), Color.Transparent)), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(baseColor)
                .border(2.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.5f), Color.Transparent)), CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggle
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Rounded.Shield, null, tint = activeColor, modifier = Modifier.size(64.dp))
                Text(
                    if (isActive) "PROTECTED" else "DISABLED",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun GlassStatsPanel(isStrict: Boolean, blockedCount: Int, whitelistedCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(LiquidGlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
            .padding(24.dp)
    ) {
        Column {
            Text("NETWORK OVERVIEW", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("DEFAULT POLICY", if (isStrict) "STRICT" else "WHITELIST", if (isStrict) NeonRed else AccentPurple)
                StatItem("BLOCKED TODAY", "$blockedCount PKTS", NeonGreen)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("WHITELISTED", "$whitelistedCount APPS", TextPrimary)
                StatItem("THREATS", "0 DETECTED", NeonGreen)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column {
        Text(label, color = TextSecondary, fontSize = 10.sp)
        Text(value, color = color, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
    }
}
