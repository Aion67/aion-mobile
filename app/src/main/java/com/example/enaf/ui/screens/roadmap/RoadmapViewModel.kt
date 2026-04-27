package com.example.enaf.ui.screens.roadmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoadmapViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoadmapUiState())
    val uiState: StateFlow<RoadmapUiState> = _uiState.asStateFlow()

    private var currentUserId: String? = null

    init {
        observeActiveSession()
    }

    fun onEvent(event: RoadmapUiEvent) {
        when (event) {
            RoadmapUiEvent.Refresh -> refresh()
        }
    }

    private fun observeActiveSession() {
        viewModelScope.launch {
            localRepository.observeActiveSession().collect { session ->
                val userId = session?.userId
                if (userId == null) {
                    _uiState.value = RoadmapUiState(
                        subtitle = "No active profile. Start a guest session to unlock milestones.",
                        isLoading = false,
                    )
                    return@collect
                }

                currentUserId = userId
                loadRoadmapState(userId)
            }
        }
    }

    private fun refresh() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            loadRoadmapState(userId)
        }
    }

    private suspend fun loadRoadmapState(userId: String) {
        val summaries = localRepository.getDailySummaries(userId).sortedBy { it.summaryDate }
        val achievements = localRepository.getAchievementCache(userId)

        val streak = summaries.maxOfOrNull { it.streakDay } ?: 0
        val focusXp = summaries.sumOf { it.focusMinutes }
        val lowScrollDays = summaries.count { it.scrollMinutes <= 120 }
        val averageCredit = summaries.map { it.creditScore }.average().toInt()

        _uiState.value = RoadmapUiState(
            subtitle = buildSubtitle(achievements.size, averageCredit),
            milestones = buildMilestones(
                streak = streak,
                lowScrollDays = lowScrollDays,
                focusXp = focusXp,
                totalDays = summaries.size,
            ),
            isLoading = false,
        )
    }

    private fun buildSubtitle(achievementCount: Int, averageCredit: Int): String {
        return "Unlocked badges: $achievementCount. Average focus score: $averageCredit. Keep pushing your weekly milestones."
    }

    private fun buildMilestones(
        streak: Int,
        lowScrollDays: Int,
        focusXp: Int,
        totalDays: Int,
    ): List<RoadmapMilestoneUiModel> {
        val recruitProgress = streak.coerceAtMost(7)
        val sentinelProgress = lowScrollDays.coerceAtMost(5)
        val veteranProgress = streak.coerceAtMost(10)
        val vanguardProgress = focusXp.coerceAtMost(2000)

        return listOf(
            RoadmapMilestoneUiModel(
                id = "week-1",
                title = "Week 1: Recruit",
                detail = "Track 3 antagonist apps for 7 days",
                progressLabel = "$recruitProgress/7 days",
                isCompleted = recruitProgress >= 7,
            ),
            RoadmapMilestoneUiModel(
                id = "week-2",
                title = "Week 2: Sentinel",
                detail = "Keep daily social usage under 2h for 5 days",
                progressLabel = "$sentinelProgress/5 days",
                isCompleted = sentinelProgress >= 5,
            ),
            RoadmapMilestoneUiModel(
                id = "week-3",
                title = "Week 3: Veteran",
                detail = "Hit a 10-day focus streak",
                progressLabel = if (veteranProgress >= 10) "Completed" else "$veteranProgress/10 days",
                isCompleted = veteranProgress >= 10,
            ),
            RoadmapMilestoneUiModel(
                id = "week-4",
                title = "Week 4: Vanguard",
                detail = "Earn 2,000 focus XP total",
                progressLabel = "$vanguardProgress / 2,000 XP in $totalDays days",
                isCompleted = vanguardProgress >= 2000,
            ),
        )
    }
}
