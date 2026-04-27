package com.example.enaf.ui.screens.auth

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
fun AuthRoute(
    onAuthSuccess: () -> Unit = {},
    initialMode: AuthMode = AuthMode.SIGN_UP,
) {
    val context = LocalContext.current
    val localRepository = remember(context) {
        RoomLocalRepository(EnafDatabaseProvider.get(context))
    }
    val viewModel = remember(localRepository) { AuthViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(localRepository) {
        ensureLocalSeedData(localRepository)
    }

    AuthScreen(
        uiState = if (uiState.mode == initialMode) uiState else uiState.copy(mode = initialMode),
        onEvent = { event -> viewModel.onEvent(event, onAuthSuccess) },
    )
}
