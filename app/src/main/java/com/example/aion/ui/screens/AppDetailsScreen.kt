package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.data.entities.UsageSessionEntity
import com.example.aion.ui.components.*
import com.example.aion.ui.theme.Variables
import com.example.aion.ui.viewmodels.AppDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppDetailsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("Settings saved successfully")
            viewModel.dismissSaveSuccess()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AionTopAppBar(
                title = uiState.appName,
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppDetailsHeader(
                    icon = uiState.icon,
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
                        0 -> OverviewTabContent(
                            usageTodayMs = uiState.usageTodayMs,
                            limitMs = uiState.currentLimitMs
                        )
                        1 -> SettingsTabContent(
                            limitMs = uiState.pendingLimitMs,
                            onLimitChange = { h, m, s -> viewModel.updatePendingLimit(h, m, s) },
                            trackingEnabled = uiState.pendingIsTracked,
                            onTrackingChange = { viewModel.updatePendingTracking(it) },
                            isDirty = uiState.isDirty,
                            onSave = { viewModel.saveSettings() }
                        )
                        2 -> HistoryTabContent(history = uiState.history, dailyLimitMs = uiState.currentLimitMs)
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewTabContent(usageTodayMs: Long, limitMs: Long) {
    val progress = if (limitMs > 0) usageTodayMs.toFloat() / limitMs.toFloat() else 0f
    val remainingMs = maxOf(0L, limitMs - usageTodayMs)
    
    // User requested default scores to be zero. 
    // Showing 0 if no limit set OR no usage recorded today.
    val score = if (limitMs > 0 && usageTodayMs > 0) {
        (1f - minOf(1f, progress)) * 100
    } else {
        0f
    }

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
                progress = if (limitMs > 0) minOf(1f, progress) else 0f,
                valueText = String.format(Locale.getDefault(), "%.2f", score),
                metricText = "Score"
            )
            AionProgressGauge(
                progress = if (limitMs > 0) 1f - minOf(1f, progress) else 0f,
                valueText = formatDuration(remainingMs),
                metricText = "Remaining",
                progressColor = MaterialTheme.colorScheme.primary
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
    limitMs: Long,
    onLimitChange: (Int, Int, Int) -> Unit,
    trackingEnabled: Boolean,
    onTrackingChange: (Boolean) -> Unit,
    isDirty: Boolean,
    onSave: () -> Unit
) {
    val totalSeconds = limitMs / 1000
    val h = (totalSeconds / 3600).toInt()
    val m = ((totalSeconds % 3600) / 60).toInt()
    val s = (totalSeconds % 60).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Daily Limit",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            AionLimitPicker(hours = h, minutes = m, seconds = s, onValueChange = onLimitChange)
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
            onClick = onSave,
            enabled = isDirty,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HistoryTabContent(history: List<UsageSessionEntity>, dailyLimitMs: Long) {
    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No history available", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    // Group sessions by day
    val groupedHistory = history.groupBy {
        val cal = Calendar.getInstance().apply { timeInMillis = it.startTime }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }.toList().sortedByDescending { it.first }

    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(groupedHistory) { (dayStart, sessions) ->
            val totalUsedMs = sessions.sumOf { it.totalDurationMs }
            val isExceeded = dailyLimitMs > 0 && totalUsedMs > dailyLimitMs
            val percentage = if (dailyLimitMs > 0) (totalUsedMs * 100 / dailyLimitMs).toInt() else 0
            
            AionHistoryItem(
                date = dateFormat.format(Date(dayStart)),
                day = dayFormat.format(Date(dayStart)),
                usedTime = formatDuration(totalUsedMs),
                limitTime = if (dailyLimitMs > 0) formatDuration(dailyLimitMs) else "No Limit",
                percentage = percentage,
                isExceeded = isExceeded
            )
        }
    }
}

private fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}

@Preview(showBackground = true)
@Composable
fun AppDetailsScreenPreview() {
    // Note: This preview won't work easily with hiltViewModel() without more setup
    // But keeping it for structure
}
