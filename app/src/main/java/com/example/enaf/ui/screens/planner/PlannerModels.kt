package com.example.enaf.ui.screens.planner

import com.example.enaf.ui.components.UiError
import kotlin.collections.emptyList
import kotlin.collections.listOf

data class PlannerUiState(
    val searchQuery: String = "",
    val globalLimitMinutes: Int = 0,
    val totalUsedMinutes: Int = 0,
    val usageInsightMessage: String = "",
    val allAppItems: List<PlannerAppItemUiModel> = emptyList(),
    val appItems: List<PlannerAppItemUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiError? = null,
)

data class PlannerAppItemUiModel(
    val id: String,
    val appName: String,
    val usedMinutes: Int,
    val limitMinutes: Int,
    val remainingMinutes: Int,
    val usedTimeLabel: String,
    val limitTimeLabel: String,
    val remainingTimeLabel: String,
    val progress: Float,
    val accentColor: Long,
)

sealed interface PlannerUiEvent {
    data class SearchQueryChanged(val value: String) : PlannerUiEvent
    data object Refresh : PlannerUiEvent
}

fun plannerPreviewState(): PlannerUiState {
    return PlannerUiState(
        searchQuery = "",
        globalLimitMinutes = 240,
        totalUsedMinutes = 77,
        usageInsightMessage = "Your social media usage is down 12% from last week. Keep it up!",
        appItems = listOf(
            PlannerAppItemUiModel(
                id = "tiktok",
                appName = "TikTok",
                usedMinutes = 45,
                limitMinutes = 60,
                remainingMinutes = 15,
                usedTimeLabel = "+45m used",
                limitTimeLabel = "Limit: 1h",
                remainingTimeLabel = "15m remaining",
                progress = 0.75f,
                accentColor = 0xFF00F2EA,
            ),
            PlannerAppItemUiModel(
                id = "youtube",
                appName = "YouTube",
                usedMinutes = 80,
                limitMinutes = 120,
                remainingMinutes = 40,
                usedTimeLabel = "+1h 20m",
                limitTimeLabel = "Limit: 2h",
                remainingTimeLabel = "40m remaining",
                progress = 0.66f,
                accentColor = 0xFFFF0000,
            ),
            PlannerAppItemUiModel(
                id = "instagram",
                appName = "Instagram",
                usedMinutes = 12,
                limitMinutes = 45,
                remainingMinutes = 33,
                usedTimeLabel = "+12m used",
                limitTimeLabel = "Limit: 45m",
                remainingTimeLabel = "33m remaining",
                progress = 0.26f,
                accentColor = 0xFFE1306C,
            ),
        ),
        isLoading = false,
    )
}
