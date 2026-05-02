package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aion.ui.viewmodels.HomeViewModel
import com.example.aion.util.TimeUtils
import com.example.aion.ui.components.*
import com.example.aion.ui.theme.Variables

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onAppClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "",
                avatarRes = com.example.aion.R.drawable.tiktok,
                actions = {
                    AionProfileActionButton(
                        avatarRes = com.example.aion.R.drawable.tiktok,
                        onClick = onProfileClick
                    )
                }
            )
        },
        containerColor = Variables.SchemesSurface
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AionHomeHeader(userName = "Alex", avatarRes = com.example.aion.R.drawable.tiktok)
            }

            item {
                val totalUsage = uiState.trackedApps.sumOf { it.usageMs }
                val totalLimit = uiState.trackedApps.sumOf { it.limitMs }
                val usagePercentage = if (totalLimit > 0) totalUsage.toFloat() / totalLimit else 0f

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AionProgressGauge(
                        progress = 0.7623f, // Score logic still static for now
                        valueText = "76.23",
                        metricText = "Score",
                        modifier = Modifier.weight(1f)
                    )

                    AionProgressGauge(
                        progress = usagePercentage,
                        valueText = "${(usagePercentage * 100).toInt()}%",
                        metricText = "Today",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                AionStatCard(percentage = 30, label = "Improvement from yesterday")
            }

            item {
                AionStatCard(percentage = -10, label = "Improvement from last week")
            }

            items(uiState.trackedApps) { trackedAppUsage ->
                val remainingMs = (trackedAppUsage.limitMs - trackedAppUsage.usageMs).coerceAtLeast(0)
                val progress = if (trackedAppUsage.limitMs > 0) trackedAppUsage.usageMs.toFloat() / trackedAppUsage.limitMs else 0f

                PlanAppCard(
                    appName = trackedAppUsage.app.appName,
                    icon = trackedAppUsage.icon,
                    creditScore = "76.23", // Static for now
                    usedTime = TimeUtils.formatDuration(trackedAppUsage.usageMs),
                    remainingTime = TimeUtils.formatDuration(remainingMs),
                    progress = progress,
                    onClick = { onAppClick(trackedAppUsage.app.packageName) }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}