package com.example.enaf.ui.screens.leaderboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.enaf.data.local.EnafDatabaseProvider
import com.example.enaf.data.repository.RoomLocalRepository
import com.example.enaf.data.seed.ensureLocalSeedData

@Composable
fun LeaderboardRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val localRepository = remember(context) {
        RoomLocalRepository(EnafDatabaseProvider.get(context))
    }
    val viewModel = remember(localRepository) { LeaderboardViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(localRepository) {
        ensureLocalSeedData(localRepository)
    }

    LeaderboardScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
