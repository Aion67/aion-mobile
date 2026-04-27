package com.example.enaf.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.enaf.data.local.EnafDatabaseProvider
import com.example.enaf.data.repository.RoomLocalRepository
import com.example.enaf.data.seed.ensureLocalSeedData

@Composable
fun OnboardingRoute(
    onGetStartedClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val localRepository = remember(context) {
        RoomLocalRepository(EnafDatabaseProvider.get(context))
    }
    val viewModel = remember(localRepository) { OnboardingViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(localRepository) {
        ensureLocalSeedData(localRepository)
    }

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
