package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var appToConfirmDelete by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(title = "", containerColor = Color.Transparent)
        },
        containerColor = Color.Transparent
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = inner.calculateTopPadding(),
                bottom = 140.dp // Extra space for floating bottom bar
            ),
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
                if (uiState.streakDays.isNotEmpty()) {
                    AionStreakBar(days = uiState.streakDays)
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

            item {
                SortHeader(title = "Your Plan")
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
                    onClick = { onAppClick(trackedAppUsage.app.packageName) },
                    onLongClick = { appToConfirmDelete = trackedAppUsage.app.packageName }
                )
            }
        }
    }

    if (appToConfirmDelete != null) {
        AlertDialog(
            onDismissRequest = { appToConfirmDelete = null },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeApp(appToConfirmDelete!!)
                    appToConfirmDelete = null
                }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { appToConfirmDelete = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Remove App from Plan?") },
            text = { Text("Are you sure you want to stop tracking this app? Your progress will be saved but limits will be disabled.") },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
