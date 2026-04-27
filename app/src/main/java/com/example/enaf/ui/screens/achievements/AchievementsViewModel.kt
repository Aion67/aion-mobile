package com.example.enaf.ui.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: AchievementsUiEvent) {
        when (event) {
            AchievementsUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = AchievementsUiState(
                        unlockedLabel = "0 / 4 unlocked",
                        items = achievementCatalog().map { it.copy(unlocked = false) },
                        isLoading = false,
                    )
                    return@collect
                }
                currentUserId = userId
                loadAchievements(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadAchievements(userId)
        }
    }

    private suspend fun loadAchievements(userId: String) {
        val unlockedIds = localRepository.getAchievementCache(userId).map { it.achievementId }.toSet()
        val items = achievementCatalog().map { base ->
            base.copy(unlocked = unlockedIds.contains(base.id))
        }
        val unlockedCount = items.count { it.unlocked }

        _uiState.value = AchievementsUiState(
            unlockedLabel = "$unlockedCount / ${items.size} unlocked",
            items = items,
            isLoading = false,
        )
    }

    private fun achievementCatalog(): List<AchievementItemUiModel> {
        return listOf(
            AchievementItemUiModel("3-day-blackout", "3-Day Blackout", "No social app limit breaks for 3 days", false),
            AchievementItemUiModel("night-discipline", "Night Discipline", "No scrolling after 10 PM for 5 days", false),
            AchievementItemUiModel("diamond-mind", "Diamond Mind", "Earn 100 diamonds", false),
            AchievementItemUiModel("veteran-streak", "Veteran Streak", "Maintain a 10-day focus streak", false),
        )
    }
}
