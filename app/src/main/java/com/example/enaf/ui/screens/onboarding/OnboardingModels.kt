package com.example.enaf.ui.screens.onboarding

import com.example.enaf.ui.components.UiError
import kotlin.collections.listOf

data class OnboardingPageUiModel(
    val title: String,
    val subtitle: String,
)

data class OnboardingUiState(
    val currentPageIndex: Int = 0,
    val pages: List<OnboardingPageUiModel> = defaultOnboardingPages(),
    val ctaText: String = "Get Started",
    val error: UiError? = null,
)

sealed interface OnboardingUiEvent {
    data object NextClicked : OnboardingUiEvent
    data object SkipClicked : OnboardingUiEvent
    data object GetStartedClicked : OnboardingUiEvent
    data object LoginClicked : OnboardingUiEvent
}

fun onboardingPreviewState(): OnboardingUiState {
    return OnboardingUiState()
}

fun defaultOnboardingPages(): List<OnboardingPageUiModel> {
    return listOf(
        OnboardingPageUiModel(
            title = "Reclaim Your Focus",
            subtitle = "Experience the pulse of futuristic wellness and break free from digital noise.",
        ),
        OnboardingPageUiModel(
            title = "Train Discipline Daily",
            subtitle = "Set limits, trigger ambushes, and convert time saved into XP and rewards.",
        ),
        OnboardingPageUiModel(
            title = "Build Your Warrior Profile",
            subtitle = "Track streaks, unlock achievements, and compete with stronger digital habits.",
        ),
    )
}
