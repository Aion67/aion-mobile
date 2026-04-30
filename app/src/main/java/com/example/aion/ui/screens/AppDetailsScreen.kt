package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.components.*
import com.example.aion.ui.theme.Variables

@Composable
fun AppDetailsScreen(
    appName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    // Settings State
    var limitHours by remember { mutableStateOf(1) }
    var limitMinutes by remember { mutableStateOf(30) }
    var limitSeconds by remember { mutableStateOf(0) }
    var isTrackingEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = appName,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack
            )
        },
        containerColor = Variables.SchemesSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Persistent Header
            AppDetailsHeader(
                iconRes = R.drawable.tiktok,
                lastOpened = "13:08",
                dataUsage = "34 MB",
                notoriety = "HARD",
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Tabs
            AionTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            // Tab Content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTabIndex) {
                    0 -> OverviewTabContent()
                    1 -> SettingsTabContent(
                        h = limitHours,
                        m = limitMinutes,
                        s = limitSeconds,
                        onTimeChange = { h, m, s ->
                            limitHours = h
                            limitMinutes = m
                            limitSeconds = s
                        },
                        trackingEnabled = isTrackingEnabled,
                        onTrackingChange = { isTrackingEnabled = it }
                    )
                    2 -> HistoryTabContent()
                }
            }
        }
    }
}

@Composable
private fun OverviewTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AionProgressGauge(
                progress = 0.7623f,
                valueText = "76.23",
                metricText = "Score"
            )
            AionProgressGauge(
                progress = 0.45f,
                valueText = "4h 23m",
                metricText = "Used",
                progressColor = Variables.PrimaryBrand
            )
        }

        AionStreakBar(
            days = listOf(
                StreakDay("Mon", "01", true),
                StreakDay("Tue", "02", true),
                StreakDay("Wed", "03", true, isToday = true),
                StreakDay("Thu", "04", false),
                StreakDay("Fri", "05", false),
                StreakDay("Sat", "06", false),
                StreakDay("Sun", "07", false)
            )
        )
        
        AionFilledButton(
            text = "Reset Progress",
            onClick = { /* Handle reset */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingsTabContent(
    h: Int, m: Int, s: Int,
    onTimeChange: (Int, Int, Int) -> Unit,
    trackingEnabled: Boolean,
    onTrackingChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            androidx.compose.material3.Text(
                text = "Daily Limit",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            AionLimitPicker(hours = h, minutes = m, seconds = s, onValueChange = onTimeChange)
        }

        AionToggleCard(
            label = "Enable Tracking",
            description = "Track and limit usage for this application",
            checked = trackingEnabled,
            onCheckedChange = onTrackingChange
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        AionFilledButton(
            text = "Save Settings",
            onClick = { /* Handle save */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HistoryTabContent() {
    val historyItems = listOf(
        HistoryData("01 Oct", "Mon", "1h 45m", "1h 30m", 116, true),
        HistoryData("30 Sep", "Sun", "50m", "1h 30m", 55, false),
        HistoryData("29 Sep", "Sat", "1h 10m", "1h 30m", 77, false),
        HistoryData("28 Sep", "Fri", "2h 05m", "1h 30m", 138, true)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(historyItems) { item ->
            AionHistoryItem(
                date = item.date,
                day = item.day,
                usedTime = item.usedTime,
                limitTime = item.limitTime,
                percentage = item.percentage,
                isExceeded = item.isExceeded
            )
        }
    }
}

private data class HistoryData(
    val date: String,
    val day: String,
    val usedTime: String,
    val limitTime: String,
    val percentage: Int,
    val isExceeded: Boolean
)

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    AppDetailsScreen(appName = "TikTok", onBack = {})
}
