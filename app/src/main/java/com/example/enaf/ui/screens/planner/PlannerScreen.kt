package com.example.enaf.ui.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import com.example.enaf.ui.components.EmptyStateCard
import com.example.enaf.ui.components.EnafTextField
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.components.GlobalLimitCard
import com.example.enaf.ui.components.LoadingStateText
import com.example.enaf.ui.components.ScreenTitleBlock
import com.example.enaf.ui.components.SectionHeaderRow
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafTextMuted
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun PlannerScreen(
    modifier: Modifier = Modifier,
    uiState: PlannerUiState = plannerPreviewState(),
    onEvent: (PlannerUiEvent) -> Unit = {},
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
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column {
                    ScreenTitleBlock(
                        title = "Screen Time Planner",
                        subtitle = "Optimize your focus and digital wellbeing.",
                    )
                }
            }

            item {
                GlobalLimitCard()
            }

            item {
                EnafTextField(
                    value = uiState.searchQuery,
                    onValueChange = { onEvent(PlannerUiEvent.SearchQueryChanged(it)) },
                    placeholder = "Search apps...",
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = EnafTextMuted)
                    }
                )
            }

            item {
                SectionHeaderRow(
                    title = "Individual App Limits",
                    trailingText = "${uiState.appItems.size} Tracked",
                )
            }

            if (uiState.isLoading) {
                item {
                    LoadingStateText("Loading planner data...")
                }
            } else if (uiState.appItems.isEmpty()) {
                item {
                    EmptyStateCard("No tracked apps yet. Add your first antagonist app to start the war for your focus.")
                }
            } else {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.appItems.forEach { item ->
                            AppUsageItem(
                                appName = item.appName,
                                usedTime = item.usedTimeLabel,
                                limitTime = item.limitTimeLabel,
                                remainingTime = item.remainingTimeLabel,
                                progress = item.progress,
                                accentColor = Color(item.accentColor)
                            )
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EnafActionBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .border(1.dp, EnafActionBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(EnafActionBlue.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = uiState.usageInsightMessage,
                            color = EnafTextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlannerScreenPreview() {
    EnafTheme(darkTheme = true) {
        PlannerScreen()
    }
}
