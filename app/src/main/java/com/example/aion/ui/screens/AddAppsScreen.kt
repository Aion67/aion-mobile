package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// use Icons.AutoMirrored.Filled.ArrowBack via Icons import
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aion.ui.viewmodels.AddAppsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.components.AddAppCard
import com.example.aion.ui.components.AionSearchBar
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables

@Composable
fun AddAppsScreen(
    modifier: Modifier = Modifier,
    viewModel: AddAppsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                AionTopAppBar(
                    title = "",
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onLeadingClick = onNavigateBack
                )
                AionSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onClear = { viewModel.onSearchQueryChange("") }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
        ) {
            SortHeader(
                title = "Apps",
                onSortClick = { /* Handle sort */ }
            )
            
            if (uiState.isLoading) {
                // Show loading indicator
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.apps) { app ->
                        AddAppCard(
                            appName = app.name,
                            icon = app.icon,
                            isTracked = app.isTracked,
                            onToggleTracking = { viewModel.toggleTracking(app) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAppsScreenPreview() {
    AddAppsScreen()
}
