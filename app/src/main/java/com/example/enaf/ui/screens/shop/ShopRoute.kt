package com.example.enaf.ui.screens.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.enaf.ui.screens.rememberSeededLocalRepository

@Composable
fun ShopRoute(
    modifier: Modifier = Modifier,
) {
    val localRepository = rememberSeededLocalRepository()
    val viewModel = remember(localRepository) { ShopViewModel(localRepository) }
    val uiState by viewModel.uiState.collectAsState()

    ShopScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
