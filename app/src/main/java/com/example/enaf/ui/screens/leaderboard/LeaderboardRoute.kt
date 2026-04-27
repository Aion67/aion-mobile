package com.example.enaf.ui.screens.leaderboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun LeaderboardRoute(
    modifier: Modifier = Modifier,
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository) { LeaderboardViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    LeaderboardScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
