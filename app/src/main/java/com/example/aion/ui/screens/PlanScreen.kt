package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.PlanAppCard
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.ui.viewmodels.PlanViewModel
import com.example.aion.util.TimeUtils
import java.util.Locale

@Composable
fun PlanScreen(
    modifier: Modifier = Modifier,
    viewModel: PlanViewModel = hiltViewModel(),
    onNavigateToAddApps: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onAppClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Plan",
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Add Apps") },
                                onClick = {
                                    showMenu = false
                                    onNavigateToAddApps()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Remove Apps") },
                                onClick = {
                                    showMenu = false
                                    // Handle remove apps
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box {
                SortHeader(
                    title = "Apps",
                    onSortClick = { showSortMenu = true }
                )
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    com.example.aion.ui.viewmodels.PlanSort.values().forEach { sort ->
                        DropdownMenuItem(
                            text = { Text(sort.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                viewModel.setSort(sort)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.trackedApps) { item ->
                    PlanAppCard(
                        appName = item.app.appName,
                        icon = item.icon,
                        creditScore = String.format(Locale.US, "%.2f", item.score),
                        usedTime = TimeUtils.formatDuration(item.usageMs),
                        remainingTime = TimeUtils.formatDuration(item.settings.dailyLimitMs),
                        progress = if (item.settings.dailyLimitMs > 0) item.usageMs.toFloat() / item.settings.dailyLimitMs else 0f,
                        onClick = { onAppClick(item.app.packageName) },
                        onDeleteClick = { viewModel.removeApp(item.app.packageName) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanScreenPreview() {
    PlanScreen()
}
