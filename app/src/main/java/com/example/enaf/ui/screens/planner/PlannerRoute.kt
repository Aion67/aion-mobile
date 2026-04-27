package com.example.enaf.ui.screens.planner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun PlannerRoute(
    modifier: Modifier = Modifier,
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository) { PlannerViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    PlannerScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
