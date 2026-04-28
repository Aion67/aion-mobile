package com.example.enaf.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enaf.ui.components.AppUsageItem
import com.example.enaf.ui.components.EmptyStateCard
import com.example.enaf.ui.components.DailySummaryCard
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.components.LoadingStateText
import com.example.enaf.ui.components.MotivationalCard
import com.example.enaf.ui.components.ScreenTitleBlock
import com.example.enaf.ui.components.SectionHeaderRow
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = homePreviewState(),
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            EnafTopAppBar(
                onProfileClick = { /* TODO */ },
                onNotificationClick = { /* TODO */ }
            )
        },
        containerColor = EnafDarkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                ScreenTitleBlock(
                    title = "Mission Control",
                    subtitle = "Track your habits, reclaim time, and keep momentum.",
                )
            }

            // Daily Summary Hero
            item {
                DailySummaryCard(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    progress = uiState.progress,
                    hoursReclaimed = uiState.hoursReclaimed,
                )
            }

            // Today's Habits Section
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SectionHeaderRow(
                        title = "Today's Habits",
                        trailingText = if (uiState.isLoading) "Loading" else "${uiState.habits.size} Tracked",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Habit Items
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.isLoading) {
                        LoadingStateText("Loading habits...")
                    } else if (uiState.habits.isEmpty()) {
                        EmptyStateCard("No habit data yet. Start tracking apps to see your daily progress here.")
                    } else {
                        uiState.habits.forEach { habit ->
                            AppUsageItem(
                                appName = habit.appName,
                                usedTime = habit.usedTimeLabel,
                                limitTime = habit.limitTimeLabel,
                                remainingTime = habit.remainingTimeLabel,
                                progress = habit.progress,
                                accentColor = Color(habit.accentColor),
                            )
                        }
                    }
                }
            }

            // Motivational Card
            item {
                Spacer(modifier = Modifier.height(24.dp))
                MotivationalCard(
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoadingStateText(uiState.motivationalMessage)
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    EnafTheme(darkTheme = true) {
        HomeScreen()
    }
}
