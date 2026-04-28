package com.example.enaf.ui.screens.home

import com.example.enaf.ui.components.UiError
import kotlin.collections.emptyList
import kotlin.collections.listOf

data class HomeHabitUiModel(
    val id: String,
    val appName: String,
    val usedTimeLabel: String,
    val limitTimeLabel: String,
    val remainingTimeLabel: String,
    val progress: Float,
    val accentColor: Long,
)

data class HomeUiState(
    val progress: Float = 0f,
    val hoursReclaimed: Double = 0.0,
    val habits: List<HomeHabitUiModel> = emptyList(),
    val motivationalMessage: String = "",
    val isLoading: Boolean = true,
    val error: UiError? = null,
)

sealed interface HomeUiEvent {
    data object Refresh : HomeUiEvent
}

fun homePreviewState(): HomeUiState {
    return HomeUiState(
        progress = 0.8f,
        hoursReclaimed = 3.5,
        habits = listOf(
            HomeHabitUiModel(
                id = "instagram",
                appName = "Instagram",
                usedTimeLabel = "+12m used",
                limitTimeLabel = "Limit: 45m",
                remainingTimeLabel = "13m remaining",
                progress = 0.72f,
                accentColor = 0xFFE1306C,
            ),
            HomeHabitUiModel(
                id = "tiktok",
                appName = "TikTok",
                usedTimeLabel = "+45m used",
                limitTimeLabel = "Limit: 1h",
                remainingTimeLabel = "15m remaining",
                progress = 0.75f,
                accentColor = 0xFF00F2EA,
            ),
            HomeHabitUiModel(
                id = "youtube",
                appName = "YouTube",
                usedTimeLabel = "1h 20m used",
                limitTimeLabel = "Limit: 2h",
                remainingTimeLabel = "40m remaining",
                progress = 0.66f,
                accentColor = 0xFFFF0000,
            ),
        ),
        motivationalMessage = "Your focus window is strong this morning. Keep high-friction apps under their limits.",
        isLoading = false,
    )
}
