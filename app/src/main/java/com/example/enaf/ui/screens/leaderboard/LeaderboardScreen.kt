package com.example.enaf.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.components.EmptyStateCard
import com.example.enaf.ui.components.LoadingStateText
import com.example.enaf.ui.components.ScreenTitleBlock
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafCardBg
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafHeaderBorder
import com.example.enaf.ui.theme.EnafTextMuted
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    uiState: LeaderboardUiState = leaderboardPreviewState(),
    onEvent: (LeaderboardUiEvent) -> Unit = {},
) {
    Scaffold(
        topBar = { EnafTopAppBar() },
        containerColor = EnafDarkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ScreenTitleBlock(
                    title = "Global Leaderboard",
                    subtitle = uiState.subtitle,
                )
            }

            if (uiState.isLoading) {
                item {
                    LoadingStateText("Loading leaderboard...")
                }
            } else if (uiState.entries.isEmpty()) {
                item {
                    EmptyStateCard("No leaderboard data yet. Play through a few days to enter the rankings.")
                }
            } else {
                items(uiState.entries, key = { it.rank }) { entry ->
                    LeaderboardRow(
                        rank = entry.rank,
                        name = entry.name,
                        tier = entry.tier,
                        score = entry.score,
                    )
                }
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    rank: Int,
    name: String,
    tier: String,
    score: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(12.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#$rank", color = EnafActionBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Column {
                Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(tier, color = EnafTextSecondary, fontSize = 12.sp)
            }
        }
        Text(score.toString(), color = EnafTextMuted, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
private fun LeaderboardScreenPreview() {
    EnafTheme(darkTheme = true) {
        LeaderboardScreen()
    }
}
