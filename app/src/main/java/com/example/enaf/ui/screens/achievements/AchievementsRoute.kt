package com.example.enaf.ui.screens.achievements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun AchievementsRoute(
    modifier: Modifier = Modifier,
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository) { AchievementsViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    AchievementsScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
