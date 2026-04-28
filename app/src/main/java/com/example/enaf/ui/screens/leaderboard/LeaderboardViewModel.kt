package com.example.enaf.ui.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.ui.components.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: LeaderboardUiEvent) {
        when (event) {
            LeaderboardUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = LeaderboardUiState(
                        subtitle = "No active profile",
                        entries = emptyList(),
                        isLoading = false,
                    )
                    return@collect
                }
                currentUserId = userId
                loadLeaderboard(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadLeaderboard(userId)
        }
    }

    private suspend fun loadLeaderboard(userId: String) {
        val latestSummary = localRepository.getDailySummaries(userId).firstOrNull()
        val userScore = latestSummary?.creditScore ?: 700

        val entries = buildList {
            add(
                LeaderboardEntryUiModel(
                    rank = 1,
                    name = "You",
                    tier = tierForScore(userScore),
                    score = userScore,
                )
            )
            for (index in 1..7) {
                val score = (userScore - (index * 37) + 120).coerceAtLeast(500)
                add(
                    LeaderboardEntryUiModel(
                        rank = index + 1,
                        name = "Warrior-${100 + index}",
                        tier = tierForScore(score),
                        score = score,
                    )
                )
            }
        }.sortedByDescending { it.score }
            .mapIndexed { idx, item -> item.copy(rank = idx + 1) }

        _uiState.value = LeaderboardUiState(
            subtitle = "Ranked by Digital Credit Score",
            entries = entries,
            isLoading = false,
        )
    }

    private fun tierForScore(score: Int): String {
        return when {
            score >= 900 -> "Legend"
            score >= 780 -> "Veteran"
            else -> "Recruit"
        }
    }
}
