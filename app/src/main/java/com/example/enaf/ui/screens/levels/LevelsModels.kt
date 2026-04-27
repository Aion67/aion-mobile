package com.example.enaf.ui.screens.levels

import kotlin.collections.emptyList
import kotlin.collections.listOf

data class LevelUnlockUiModel(
    val levelText: String,
    val rewardText: String,
)

data class LevelsUiState(
    val level: Int = 1,
    val title: String = "Recruit",
    val currentXpInLevel: Int = 0,
    val xpToNextLevel: Int = 250,
    val unlocks: List<LevelUnlockUiModel> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface LevelsUiEvent {
    data object Refresh : LevelsUiEvent
}

fun levelsPreviewState(): LevelsUiState {
    return LevelsUiState(
        level = 12,
        title = "Veteran",
        currentXpInLevel = 148,
        xpToNextLevel = 250,
        unlocks = listOf(
            LevelUnlockUiModel("Level 13", "Lore Theme: Iron Discipline"),
            LevelUnlockUiModel("Level 14", "+1 Daily Streak Shield"),
            LevelUnlockUiModel("Level 15", "Elite Tier Leaderboard Badge"),
        ),
        isLoading = false,
    )
}
