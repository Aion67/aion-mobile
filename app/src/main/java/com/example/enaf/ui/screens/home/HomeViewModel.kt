package com.example.enaf.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = HomeUiState(
                        motivationalMessage = "No active profile. Start a guest session to unlock your daily focus dashboard.",
                        isLoading = false,
                    )
                    return@collect
                }

                currentUserId = userId
                loadHomeState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadHomeState(userId)
        }
    }

    private suspend fun loadHomeState(userId: String) {
        val trackedApps = localRepository.getTrackedApps(userId)
        val habits = trackedApps.mapNotNull { app -> buildHabitItem(app) }
        val summaries = localRepository.getDailySummaries(userId)
        val latestSummary = summaries.firstOrNull()

        val totalLimitMinutes = habits.sumOf { parseMinutesFromLimitLabel(it.limitTimeLabel) }.coerceAtLeast(1)
        val totalUsedMinutes = habits.sumOf { parseMinutesFromUsedLabel(it.usedTimeLabel) }
        val progress = (totalUsedMinutes.toFloat() / totalLimitMinutes.toFloat()).coerceIn(0f, 1f)
        val hoursReclaimed = ((latestSummary?.timeSavedMinutes ?: 0) / 60.0)

        _uiState.value = HomeUiState(
            progress = progress,
            hoursReclaimed = hoursReclaimed,
            habits = habits,
            motivationalMessage = buildMotivation(progress, latestSummary?.streakDay ?: 0),
            isLoading = false,
        )
    }

    private suspend fun buildHabitItem(app: TrackedAppEntity): HomeHabitUiModel? {
        val limit = latestLimitFor(app.id) ?: return null
        val usedMinutes = usageMinutesFor(app.id)
        val limitMinutes = limit.dailyLimitMinutes.coerceAtLeast(1)
        val remainingMinutes = (limitMinutes - usedMinutes).coerceAtLeast(0)
        val progress = (usedMinutes.toFloat() / limitMinutes.toFloat()).coerceIn(0f, 1f)

        return HomeHabitUiModel(
            id = app.id,
            appName = app.alias.ifBlank { app.packageName },
            usedTimeLabel = "${formatMinutes(usedMinutes)} used",
            limitTimeLabel = "Limit: ${formatMinutes(limitMinutes)}",
            remainingTimeLabel = "${formatMinutes(remainingMinutes)} remaining",
            progress = progress,
            accentColor = appAccentColor(app.packageName),
        )
    }

    private suspend fun latestLimitFor(trackedAppId: String): AppLimitEntity? {
        return localRepository.getAppLimits(trackedAppId).firstOrNull()
    }

    private suspend fun usageMinutesFor(trackedAppId: String): Int {
        return localRepository.getUsageSessionsInRange(
            trackedAppId = trackedAppId,
            start = 0L,
            end = Long.MAX_VALUE,
        ).sumOf { it.durationMinutes }
    }

    private fun buildMotivation(progress: Float, streak: Int): String {
        return when {
            progress <= 0.5f -> "Discipline is winning today. Keep your momentum and protect the streak."
            progress <= 0.8f -> "You are in control. Tighten limits this evening to close strong."
            else -> "You are close to the edge. Trigger an ambush and reclaim the next hour."
        } + " Current streak: $streak days."
    }

    private fun appAccentColor(packageName: String): Long {
        return when {
            packageName.contains("tiktok", ignoreCase = true) -> 0xFF00F2EA
            packageName.contains("youtube", ignoreCase = true) -> 0xFFFF0000
            packageName.contains("instagram", ignoreCase = true) -> 0xFFE1306C
            else -> 0xFF007BFF
        }
    }

    private fun formatMinutes(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return when {
            hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
            hours > 0 -> "${hours}h"
            else -> "${remainingMinutes}m"
        }
    }

    private fun parseMinutesFromUsedLabel(label: String): Int {
        return parseMinutes(label)
    }

    private fun parseMinutesFromLimitLabel(label: String): Int {
        return parseMinutes(label)
    }

    private fun parseMinutes(text: String): Int {
        val hourRegex = "(\\d+)h".toRegex()
        val minuteRegex = "(\\d+)m".toRegex()
        val hours = hourRegex.find(text)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val minutes = minuteRegex.find(text)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        return (hours * 60) + minutes
    }
}
