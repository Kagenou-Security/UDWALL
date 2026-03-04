package com.udwall.firewall.ui.apps

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.udwall.firewall.data.AppEntity
import com.udwall.firewall.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(viewModel: AppsViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allApps by viewModel.allApps.collectAsState()
    val whitelistedApps by viewModel.whitelistedApps.collectAsState()
    val blacklistedApps by viewModel.blacklistedApps.collectAsState()
    
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val currentApps = when(selectedTab) {
        0 -> allApps
        1 -> whitelistedApps
        else -> blacklistedApps
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkBackgroundStart, DarkBackgroundEnd)))
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LiquidGlassSurface),
            placeholder = { Text("Search apps...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Rounded.Search, null, tint = TextSecondary) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = TextPrimary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        // Glass Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = TextPrimary,
            edgePadding = 24.dp,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AccentPink
                )
            }
        ) {
            listOf("ALL", "WHITELISTED", "BLACKLISTED").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Apps List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(currentApps, key = { it.packageName }) { app ->
                AppGlassItem(
                    app = app,
                    onToggle = { viewModel.toggleWhitelist(app.packageName, it) }
                )
            }
        }
    }
}

@Composable
fun AppGlassItem(app: AppEntity, onToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    val appIcon = remember(app.packageName) {
        try {
            context.packageManager.getApplicationIcon(app.packageName)
        } catch (e: Exception) {
            null
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(LiquidGlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (appIcon != null) {
                Image(
                    painter = rememberDrawablePainter(drawable = appIcon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Android,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column(Modifier.weight(1f)) {
            Text(app.appName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
            Text(app.packageName, color = TextSecondary, fontSize = 11.sp, maxLines = 1)
        }
        
        Switch(
            checked = app.isWhitelisted,
            onCheckedChange = onToggle,
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
