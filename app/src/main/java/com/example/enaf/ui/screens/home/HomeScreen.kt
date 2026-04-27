package com.example.enaf.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.AppUsageItem
import com.example.enaf.ui.components.DailySummaryCard
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.components.MotivationalCard
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
            // Daily Summary Hero
            item {
                DailySummaryCard(
                    modifier = Modifier.padding(24.dp),
                    progress = uiState.progress,
                    hoursReclaimed = uiState.hoursReclaimed,
                )
            }

            // Today's Habits Section
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Habits",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (uiState.isLoading) "Loading" else "${uiState.habits.size} Tracked",
                            color = Color(0xFF007BFF),
                            fontSize = 14.sp
                        )
                    }
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
                        Text(
                            text = "Loading habits...",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                        )
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
                Text(
                    text = uiState.motivationalMessage,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
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
