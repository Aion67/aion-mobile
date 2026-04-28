package com.example.enaf.ui.screens.auth

import com.example.enaf.ui.components.UiError

enum class AuthMode {
    SIGN_UP,
    LOGIN,
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.SIGN_UP,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: UiError? = null,
)

sealed interface AuthUiEvent {
    data class EmailChanged(val value: String) : AuthUiEvent
    data class PasswordChanged(val value: String) : AuthUiEvent
    data object ToggleModeClicked : AuthUiEvent
    data object SubmitClicked : AuthUiEvent
    data object GoogleClicked : AuthUiEvent
    data object AppleClicked : AuthUiEvent
    data object ContinueAsGuestClicked : AuthUiEvent
}

fun authPreviewState(): AuthUiState {
    return AuthUiState(mode = AuthMode.SIGN_UP)
}
