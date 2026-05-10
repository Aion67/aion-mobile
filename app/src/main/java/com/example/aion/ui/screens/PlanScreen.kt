package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.aion.ui.components.*
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
    var appToConfirmDelete by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Plan",
                containerColor = Color.Transparent,
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        AionGlassMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            items = listOf(
                                "Add Apps" to {
                                    showMenu = false
                                    onNavigateToAddApps()
                                }
                            )
                        )
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
            
            Box(modifier = Modifier.fillMaxWidth()) {
                SortHeader(
                    title = "Apps",
                    onSortClick = { showSortMenu = !showSortMenu }
                )
                
                Box(
                    modifier = Modifier
                        .padding(top = 48.dp, end = 16.dp)
                        .zIndex(10f)
                        .align(androidx.compose.ui.Alignment.TopEnd)
                ) {
                    AionGlassMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        items = com.example.aion.ui.viewmodels.PlanSort.values().map { sort ->
                            sort.name.lowercase().replaceFirstChar { it.uppercase() } to {
                                viewModel.setSort(sort)
                                showSortMenu = false
                            }
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp).let {
                    PaddingValues(
                        start = it.calculateStartPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                        end = it.calculateEndPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                        top = it.calculateTopPadding(),
                        bottom = 140.dp
                    )
                },
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
                        onLongClick = { appToConfirmDelete = item.app.packageName }
                    )
                }
            }
        }
    }

    if (appToConfirmDelete != null) {
        AlertDialog(
            onDismissRequest = { appToConfirmDelete = null },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeApp(appToConfirmDelete!!)
                    appToConfirmDelete = null
                }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { appToConfirmDelete = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Remove App from Plan?") },
            text = { Text("Are you sure you want to stop tracking this app? Your progress will be saved but limits will be disabled.") },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlanScreenPreview() {
    PlanScreen()
}
