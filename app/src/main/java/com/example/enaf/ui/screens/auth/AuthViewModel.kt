package com.example.enaf.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enaf.data.local.entity.UserSessionEntity
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.ui.components.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val localRepository: LocalRepository,
    initialMode: AuthMode = AuthMode.SIGN_UP,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(mode = initialMode))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEvent(event: AuthUiEvent, onAuthSuccess: () -> Unit = {}) {
        when (event) {
            is AuthUiEvent.EmailChanged -> _uiState.value = _uiState.value.copy(email = event.value, emailError = null, error = null)
            is AuthUiEvent.PasswordChanged -> _uiState.value = _uiState.value.copy(password = event.value, passwordError = null, error = null)
            AuthUiEvent.ToggleModeClicked -> toggleMode()
            AuthUiEvent.SubmitClicked -> submitCredentials(onAuthSuccess)
            AuthUiEvent.GoogleClicked -> signInWithProvider("google", onAuthSuccess)
            AuthUiEvent.AppleClicked -> signInWithProvider("apple", onAuthSuccess)
            AuthUiEvent.ContinueAsGuestClicked -> continueAsGuest(onAuthSuccess)
        }
    }

    private fun toggleMode() {
        val next = if (_uiState.value.mode == AuthMode.SIGN_UP) AuthMode.LOGIN else AuthMode.SIGN_UP
        _uiState.value = _uiState.value.copy(mode = next, emailError = null, passwordError = null, error = null)
    }

    private fun submitCredentials(onAuthSuccess: () -> Unit) {
        val state = _uiState.value
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _uiState.value = state.copy(
                emailError = emailError,
                passwordError = passwordError,
                error = UiError.ValidationError("Fix the highlighted fields and try again."),
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, emailError = null, passwordError = null, error = null)
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
                _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                onAuthSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.toUiError(),
                )
            }
        }
    }

    private fun signInWithProvider(provider: String, onAuthSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, emailError = null, passwordError = null, error = null)
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
                _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                onAuthSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.toUiError(),
                )
            }
        }
    }

    private fun continueAsGuest(onAuthSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, emailError = null, passwordError = null, error = null)
                localRepository.upsertSession(
                    UserSessionEntity(
                        id = "session-guest",
                        userId = "guest-local-user",
                        authToken = null,
                        onboardingSeen = true,
                        createdAtEpochMillis = System.currentTimeMillis(),
                    )
                )
                _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                onAuthSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.toUiError(),
                )
            }
        }
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email is required."
        if (!email.contains("@") || !email.contains(".")) return "Enter a valid email address."
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password is required."
        if (password.length < 6) return "Password must be at least 6 characters."
        return null
    }
}
