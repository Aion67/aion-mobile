package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.PlanAppCard
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables

data class AppPlan(
    val name: String,
    val iconRes: Int,
    val creditScore: String,
    val usedTime: String,
    val remainingTime: String,
    val progress: Float
)

val sampleAppPlans = listOf(
    AppPlan("Instagram", R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.8f),
    AppPlan("TikTok", R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.5f),
    AppPlan("YouTube", R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.2f),
    AppPlan("Snapchat", R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.9f),
    AppPlan("Facebook", R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.4f)
)

@Composable
fun PlanScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddApps: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "Plan",
                avatarRes = R.drawable.tiktok, // Placeholder avatar
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Variables.SchemesOnSurface
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
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleAppPlans) { app ->
                    PlanAppCard(
                        appName = app.name,
                        iconRes = app.iconRes,
                        creditScore = app.creditScore,
                        usedTime = app.usedTime,
                        remainingTime = app.remainingTime,
                        progress = app.progress
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
