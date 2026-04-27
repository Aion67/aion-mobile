package com.example.enaf.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthUiEvent, onAuthSuccess: () -> Unit = {}) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.value = _uiState.value.copy(email = event.value, errorMessage = null)
            is AuthUiEvent.PasswordChanged -> _uiState.value = _uiState.value.copy(password = event.value, errorMessage = null)
            AuthUiEvent.ToggleModeClicked -> toggleMode()
            AuthUiEvent.SubmitClicked -> submitCredentials(onAuthSuccess)
            AuthUiEvent.GoogleClicked -> signInWithProvider("google", onAuthSuccess)
            AuthUiEvent.AppleClicked -> signInWithProvider("apple", onAuthSuccess)
            AuthUiEvent.ContinueAsGuestClicked -> continueAsGuest(onAuthSuccess)
        }
    }

    private fun toggleMode() {
        val next = if (_uiState.value.mode == AuthMode.SIGN_UP) AuthMode.LOGIN else AuthMode.SIGN_UP
        _uiState.value = _uiState.value.copy(mode = next, errorMessage = null)
    }

    private fun submitCredentials(onAuthSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Enter a valid email and password (min 6 chars).")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            val userId = state.email.trim().lowercase()
            localRepository.upsertSession(
                UserSessionEntity(
                    id = "session-$userId",
                    userId = userId,
                    authToken = "local-token-$userId",
                    onboardingSeen = true,
                    createdAtEpochMillis = System.currentTimeMillis(),
                )
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            onAuthSuccess()
        }
    }

    private fun signInWithProvider(provider: String, onAuthSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val userId = "$provider-user"
            localRepository.upsertSession(
                UserSessionEntity(
                    id = "session-$userId",
                    userId = userId,
                    authToken = "$provider-token",
                    onboardingSeen = true,
                    createdAtEpochMillis = System.currentTimeMillis(),
                )
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            onAuthSuccess()
        }
    }

    private fun continueAsGuest(onAuthSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            localRepository.upsertSession(
                UserSessionEntity(
                    id = "session-guest",
                    userId = "guest-local-user",
                    authToken = null,
                    onboardingSeen = true,
                    createdAtEpochMillis = System.currentTimeMillis(),
                )
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            onAuthSuccess()
        }
    }
}
