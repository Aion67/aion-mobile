package com.example.enaf.ui.screens.roadmap

import com.example.enaf.ui.components.UiError
import kotlin.collections.emptyList
import kotlin.collections.listOf

data class RoadmapMilestoneUiModel(
    val id: String,
    val title: String,
    val detail: String,
    val progressLabel: String,
    val isCompleted: Boolean,
)

data class RoadmapUiState(
    val subtitle: String = "",
    val milestones: List<RoadmapMilestoneUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: UiError? = null,
)

sealed interface RoadmapUiEvent {
    data object Refresh : RoadmapUiEvent
}

fun roadmapPreviewState(): RoadmapUiState {
    return RoadmapUiState(
        subtitle = "Complete milestones to level up and unlock stronger anti-distraction powers.",
        milestones = listOf(
            RoadmapMilestoneUiModel(
                id = "week-1",
                title = "Week 1: Recruit",
                detail = "Track 3 antagonist apps for 7 days",
                progressLabel = "5/7 days",
                isCompleted = false,
            ),
            RoadmapMilestoneUiModel(
                id = "week-2",
                title = "Week 2: Sentinel",
                detail = "Keep daily social usage under 2h for 5 days",
                progressLabel = "2/5 days",
                isCompleted = false,
            ),
            RoadmapMilestoneUiModel(
                id = "week-3",
                title = "Week 3: Veteran",
                detail = "Hit a 10-day focus streak",
                progressLabel = "Completed",
                isCompleted = true,
            ),
            RoadmapMilestoneUiModel(
                id = "week-4",
                title = "Week 4: Vanguard",
                detail = "Earn 2,000 focus XP total",
                progressLabel = "1,480 / 2,000 XP",
                isCompleted = false,
            ),
        ),
        isLoading = false,
    )
}
