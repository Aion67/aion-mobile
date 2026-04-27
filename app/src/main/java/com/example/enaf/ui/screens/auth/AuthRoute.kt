package com.example.enaf.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit = {},
    initialMode: AuthMode = AuthMode.SIGN_UP,
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository, initialMode) { AuthViewModel(localRepository, initialMode) }
    val uiState by viewModel.uiState.collectAsState()

    AuthScreen(
        uiState = uiState,
        onEvent = { event -> viewModel.onEvent(event, onAuthSuccess) },
    )
}
