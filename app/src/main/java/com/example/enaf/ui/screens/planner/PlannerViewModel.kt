package com.example.enaf.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.AppLimitEntity
import com.example.enaf.data.local.entity.TrackedAppEntity
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlannerViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlannerUiState())
    val uiState: StateFlow<PlannerUiState> = _uiState.asStateFlow()

    init {
        observeActiveSession()
    }

    fun onEvent(event: PlannerUiEvent) {
        when (event) {
            is PlannerUiEvent.SearchQueryChanged -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.value)
                refreshVisibleItems()
            }
            PlannerUiEvent.Refresh -> refreshVisibleItems()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = PlannerUiState(
                        isLoading = false,
                        usageInsightMessage = "No active session yet. Create a guest profile or sign in to start tracking.",
                    )
                    return@collect
                }

                loadPlannerState(userId)
            }
        }
    }

    private fun refreshVisibleItems() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            appItems = currentState.allAppItems.filter { item ->
                item.appName.contains(currentState.searchQuery, ignoreCase = true)
            },
        )
    }

    private suspend fun loadPlannerState(userId: String) {
        val trackedApps = localRepository.getTrackedApps(userId)
        val appItems = mutableListOf<PlannerAppItemUiModel>()
        trackedApps.forEach { app ->
            val item = buildPlannerItem(app)
            if (item != null) {
                appItems.add(item)
            }
        }
        val visibleItems = filterItems(appItems, _uiState.value.searchQuery)

        _uiState.value = PlannerUiState(
            searchQuery = _uiState.value.searchQuery,
            globalLimitMinutes = appItems.sumOf { it.limitMinutes },
            totalUsedMinutes = appItems.sumOf { it.usedMinutes },
            usageInsightMessage = buildInsightMessage(appItems),
            allAppItems = appItems,
            appItems = visibleItems,
            isLoading = false,
        )
    }

    private suspend fun buildPlannerItem(app: TrackedAppEntity): PlannerAppItemUiModel? {
        val latestLimit = latestLimitFor(app.id) ?: return null
        val usageMinutes = totalUsageMinutesFor(app.id)
        val limitMinutes = latestLimit.dailyLimitMinutes.coerceAtLeast(1)
        val remainingMinutes = (limitMinutes - usageMinutes).coerceAtLeast(0)
        val progress = (usageMinutes.toFloat() / limitMinutes.toFloat()).coerceIn(0f, 1f)

        return PlannerAppItemUiModel(
            id = app.id,
            appName = app.alias.ifBlank { app.packageName },
            usedMinutes = usageMinutes,
            limitMinutes = limitMinutes,
            remainingMinutes = remainingMinutes,
            usedTimeLabel = formatMinutesLabel(usageMinutes, usedPrefix = true),
            limitTimeLabel = "Limit: ${formatMinutes(limitMinutes)}",
            remainingTimeLabel = "${formatMinutes(remainingMinutes)} remaining",
            progress = progress,
            accentColor = appAccentColor(app.packageName),
        )
    }

    private suspend fun latestLimitFor(trackedAppId: String): AppLimitEntity? {
        return localRepository.getAppLimits(trackedAppId).firstOrNull()
    }

    private suspend fun totalUsageMinutesFor(trackedAppId: String): Int {
        val sessions = localRepository.getUsageSessionsInRange(
            trackedAppId = trackedAppId,
            start = 0L,
            end = Long.MAX_VALUE,
        )
        return sessions.sumOf { it.durationMinutes }
    }

    private fun filterItems(
        items: List<PlannerAppItemUiModel>,
        query: String,
    ): List<PlannerAppItemUiModel> {
        if (query.isBlank()) return items
        return items.filter { item -> item.appName.contains(query, ignoreCase = true) }
    }

    private fun buildInsightMessage(items: List<PlannerAppItemUiModel>): String {
        return if (items.isEmpty()) {
            "No tracked apps yet. Add your first antagonist app to start the war for your focus."
        } else {
            val averageProgress = items.map { it.progress }.average().toFloat()
            if (averageProgress < 0.5f) {
                "Your focus streak is still forming. Keep your limits tight and win the day."
            } else {
                "Your social media usage is trending down. Keep it up."
            }
        }
    }

    private fun formatMinutesLabel(minutes: Int, usedPrefix: Boolean = false): String {
        val prefix = if (usedPrefix) "+" else ""
        return "$prefix${formatMinutes(minutes)}"
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

    private fun appAccentColor(packageName: String): Long {
        return when {
            packageName.contains("tiktok", ignoreCase = true) -> 0xFF00F2EA
            packageName.contains("youtube", ignoreCase = true) -> 0xFFFF0000
            packageName.contains("instagram", ignoreCase = true) -> 0xFFE1306C
            else -> 0xFF007BFF
        }
    }
}
