package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.ui.viewmodels.HomeViewModel
import com.example.aion.util.TimeUtils
import java.util.Locale
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
                actions = {
                    AionProfileActionButton(
                        onClick = onProfileClick
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AionHomeHeader(
                    userName = uiState.displayName,
                    improvementPercentage = uiState.improvementPercentage,
                    onAvatarClick = onProfileClick
                )
            }

            item {
                val totalUsage = uiState.trackedApps.sumOf { it.usageMs }
                val totalLimit = uiState.trackedApps.sumOf { it.limitMs }
                val usagePercentage = if (totalLimit > 0) totalUsage.toFloat() / totalLimit else 0f

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AionProgressGauge(
                        progress = uiState.score / 100f,
                        valueText = String.format(Locale.US, "%.2f", uiState.score),
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
                val yesterdayPercentage = (uiState.improvementPercentage * 100).toInt()
                AionStatCard(
                    percentage = yesterdayPercentage,
                    label = "Improvement from yesterday",
                    progressColor = if (yesterdayPercentage >= 0) Variables.SuccessGreen else Variables.WarningRed
                )
            }

            item {
                val weeklyPercentage = (uiState.weeklyImprovementPercentage * 100).toInt()
                AionStatCard(
                    percentage = weeklyPercentage,
                    label = "Improvement from last week",
                    progressColor = if (weeklyPercentage >= 0) Variables.SuccessGreen else Variables.WarningRed
                )
            }

            items(uiState.trackedApps) { trackedAppUsage ->
                val remainingMs = (trackedAppUsage.limitMs - trackedAppUsage.usageMs).coerceAtLeast(0)
                val progress = if (trackedAppUsage.limitMs > 0) trackedAppUsage.usageMs.toFloat() / trackedAppUsage.limitMs else 0f

                PlanAppCard(
                    appName = trackedAppUsage.app.appName,
                    icon = trackedAppUsage.icon,
                    creditScore = String.format(Locale.US, "%.2f", trackedAppUsage.score),
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
