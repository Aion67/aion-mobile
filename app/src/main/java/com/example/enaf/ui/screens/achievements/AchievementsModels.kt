package com.example.enaf.ui.screens.achievements

import com.example.enaf.ui.components.UiError
import kotlin.collections.emptyList
import kotlin.collections.listOf

data class AchievementItemUiModel(
    val id: String,
    val title: String,
    val detail: String,
    val unlocked: Boolean,
)

data class AchievementsUiState(
    val unlockedLabel: String = "0 / 0 unlocked",
    val items: List<AchievementItemUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiError? = null,
)

sealed interface AchievementsUiEvent {
    data object Refresh : AchievementsUiEvent
}

fun achievementsPreviewState(): AchievementsUiState {
    return AchievementsUiState(
        unlockedLabel = "6 / 20 unlocked",
        items = listOf(
            AchievementItemUiModel("3-day-blackout", "3-Day Blackout", "No social app limit breaks for 3 days", true),
            AchievementItemUiModel("night-discipline", "Night Discipline", "No scrolling after 10 PM for 5 days", true),
            AchievementItemUiModel("diamond-mind", "Diamond Mind", "Earn 100 diamonds", false),
            AchievementItemUiModel("legend-sprint", "Legend Sprint", "Reach top 10 in weekly leaderboard", false),
        ),
        isLoading = false,
    )
}
