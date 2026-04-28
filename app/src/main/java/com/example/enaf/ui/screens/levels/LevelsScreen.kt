package com.example.enaf.ui.screens.levels

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.enaf.ui.theme.EnafProgressBg
import com.example.enaf.ui.theme.EnafTextMuted
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun LevelsScreen(
    modifier: Modifier = Modifier,
    uiState: LevelsUiState = levelsPreviewState(),
    onEvent: (LevelsUiEvent) -> Unit = {},
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ScreenTitleBlock(
                    title = "Levels & XP",
                    subtitle = "Your focus work fuels levels, perks, and stronger unlocks.",
                )
            }

            item {
                if (uiState.isLoading) {
                    LoadingStateText("Loading levels...")
                } else {
                    LevelCard(
                        level = uiState.level,
                        title = uiState.title,
                        xp = uiState.currentXpInLevel,
                        xpToNext = uiState.xpToNextLevel,
                    )
                }
            }

            item {
                Text(
                    text = "Next Unlocks",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                if (uiState.unlocks.isEmpty()) {
                    EmptyStateCard("Level rewards will appear once progress data is available.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        uiState.unlocks.forEach { unlock ->
                            UnlockRow(unlock.levelText, unlock.rewardText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelCard(level: Int, title: String, xp: Int, xpToNext: Int) {
    val progress = xp.toFloat() / xpToNext.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(14.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(EnafActionBlue.copy(alpha = 0.16f), RoundedCornerShape(10.dp))
            )
            Column {
                Text("Level $level", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(title, color = EnafTextSecondary, fontSize = 14.sp)
            }
        }

        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = EnafActionBlue,
            trackColor = EnafProgressBg,
        )

        Text("$xp / $xpToNext XP", color = EnafTextMuted, fontSize = 12.sp)
    }
}

@Composable
private fun UnlockRow(levelText: String, rewardText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(12.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(levelText, color = EnafActionBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(rewardText, color = EnafTextSecondary, fontSize = 13.sp)
    }
}

@Preview
@Composable
private fun LevelsScreenPreview() {
    EnafTheme(darkTheme = true) {
        LevelsScreen()
    }
}
