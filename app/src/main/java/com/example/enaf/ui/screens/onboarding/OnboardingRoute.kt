package com.example.enaf.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun OnboardingRoute(
    onGetStartedClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository) { OnboardingViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    OnboardingScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
            when (event) {
                OnboardingUiEvent.SkipClicked -> onSkipClick()
                OnboardingUiEvent.GetStartedClicked -> onGetStartedClick()
                OnboardingUiEvent.LoginClicked -> onLoginClick()
                OnboardingUiEvent.NextClicked -> Unit
            }
        },
    )
}
