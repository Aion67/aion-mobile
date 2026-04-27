package com.example.enaf.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.NextClicked -> nextPage()
            OnboardingUiEvent.SkipClicked -> markOnboardingSeen()
            OnboardingUiEvent.GetStartedClicked -> markOnboardingSeen()
            OnboardingUiEvent.LoginClicked -> markOnboardingSeen()
        }
    }

    private fun nextPage() {
        val state = _uiState.value
        val nextIndex = (state.currentPageIndex + 1).coerceAtMost(state.pages.lastIndex)
        _uiState.value = state.copy(
            currentPageIndex = nextIndex,
            ctaText = if (nextIndex == state.pages.lastIndex) "Get Started" else "Next",
        )
    }

    private fun markOnboardingSeen() {
        viewModelScope.launch {
            val existing = localRepository.getActiveSession()
            val now = System.currentTimeMillis()
            val session = existing?.copy(onboardingSeen = true) ?: UserSessionEntity(
                id = "session-guest",
                userId = "guest-local-user",
                authToken = null,
                onboardingSeen = true,
                createdAtEpochMillis = now,
            )
            localRepository.upsertSession(session)
        }
    }
}
