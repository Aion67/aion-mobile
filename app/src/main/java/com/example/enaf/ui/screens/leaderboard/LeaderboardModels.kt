package com.example.enaf.ui.screens.leaderboard

import kotlin.collections.emptyList
import kotlin.collections.listOf

data class LeaderboardEntryUiModel(
    val rank: Int,
    val name: String,
    val tier: String,
    val score: Int,
)

data class LeaderboardUiState(
    val subtitle: String = "",
    val entries: List<LeaderboardEntryUiModel> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface LeaderboardUiEvent {
    data object Refresh : LeaderboardUiEvent
}

fun leaderboardPreviewState(): LeaderboardUiState {
    return LeaderboardUiState(
        subtitle = "Ranked by Digital Credit Score",
        entries = listOf(
            LeaderboardEntryUiModel(rank = 1, name = "You", tier = "Legend", score = 980),
            LeaderboardEntryUiModel(rank = 2, name = "Warrior-101", tier = "Legend", score = 933),
            LeaderboardEntryUiModel(rank = 3, name = "Warrior-102", tier = "Legend", score = 886),
            LeaderboardEntryUiModel(rank = 4, name = "Warrior-103", tier = "Veteran", score = 839),
        ),
        isLoading = false,
    )
}
