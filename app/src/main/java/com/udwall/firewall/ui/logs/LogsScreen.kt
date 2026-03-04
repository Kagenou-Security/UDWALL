package com.udwall.firewall.ui.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.udwall.firewall.data.LogEntity
import com.udwall.firewall.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(viewModel: LogsViewModel = hiltViewModel()) {
    val logs by viewModel.logs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackgroundStart, DarkBackgroundEnd)))
    ) {
        TopAppBar(
            title = { Text("Network Logs", fontWeight = FontWeight.Black, color = TextPrimary) },
            actions = {
                IconButton(onClick = { viewModel.clearLogs() }) {
                    Icon(Icons.Rounded.DeleteSweep, "Clear Logs", tint = TextSecondary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        if (logs.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No activity recorded yet", color = TextSecondary)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(logs) { log ->
                    LogGlassItem(log)
                }
            }
        }
    }
}

@Composable
fun LogGlassItem(log: LogEntity) {
    val sdf = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val time = remember(log.timestamp) { sdf.format(Date(log.timestamp)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LiquidGlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (log.isBlocked) NeonRed.copy(alpha = 0.2f) else NeonGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (log.isBlocked) "×" else "✓",
                color = if (log.isBlocked) NeonRed else NeonGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        
        Spacer(Modifier.width(12.dp))
        
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(log.appName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.width(8.dp))
                Text(time, color = TextSecondary, fontSize = 10.sp)
            }
            Text("${log.protocol} → ${log.destinationIp}:${log.destinationPort}", color = TextSecondary, fontSize = 11.sp)
        }
        
        Text(
            if (log.isBlocked) "BLOCKED" else "ALLOWED",
            color = if (log.isBlocked) NeonRed else NeonGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black
        )
    }
}
