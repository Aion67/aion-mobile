package com.example.enaf.ui.screens.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LevelsViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LevelsUiState())
    val uiState: StateFlow<LevelsUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: LevelsUiEvent) {
        when (event) {
            LevelsUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = LevelsUiState(isLoading = false)
                    return@collect
                }

                currentUserId = userId
                loadLevelsState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadLevelsState(userId)
        }
    }

    private suspend fun loadLevelsState(userId: String) {
        val summaries = localRepository.getDailySummaries(userId)
        val totalXp = summaries.sumOf { it.focusMinutes + (it.timeSavedMinutes * 2) }
        val level = (totalXp / XP_PER_LEVEL).coerceAtLeast(0) + 1
        val currentXpInLevel = totalXp % XP_PER_LEVEL

        _uiState.value = LevelsUiState(
            level = level,
            title = levelTitle(level),
            currentXpInLevel = currentXpInLevel,
            xpToNextLevel = XP_PER_LEVEL,
            unlocks = nextUnlocks(level),
            isLoading = false,
        )
    }

    private fun levelTitle(level: Int): String {
        return when {
            level >= 15 -> "Vanguard"
            level >= 10 -> "Veteran"
            level >= 6 -> "Sentinel"
            else -> "Recruit"
        }
    }

    private fun nextUnlocks(level: Int): List<LevelUnlockUiModel> {
        return listOf(
            LevelUnlockUiModel("Level ${level + 1}", "Lore Theme: Iron Discipline"),
            LevelUnlockUiModel("Level ${level + 2}", "+1 Daily Streak Shield"),
            LevelUnlockUiModel("Level ${level + 3}", "Elite Tier Leaderboard Badge"),
        )
    }

    private companion object {
        private const val XP_PER_LEVEL = 250
    }
}
