package com.example.enaf.ui.screens.roadmap

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
import androidx.compose.foundation.shape.CircleShape
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
fun RoadmapScreen(
    modifier: Modifier = Modifier,
    uiState: RoadmapUiState = roadmapPreviewState(),
    onEvent: (RoadmapUiEvent) -> Unit = {},
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ScreenTitleBlock(
                    title = "Warrior Roadmap",
                    subtitle = uiState.subtitle,
                )
            }

            item {
                if (uiState.isLoading) {
                    LoadingStateText("Loading roadmap...")
                } else if (uiState.milestones.isEmpty()) {
                    EmptyStateCard("No roadmap milestones yet. Keep tracking focus and the path will populate here.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.milestones.forEach { milestone ->
                            MilestoneCard(
                                title = milestone.title,
                                detail = milestone.detail,
                                progress = milestone.progressLabel,
                                isCompleted = milestone.isCompleted,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MilestoneCard(
    title: String,
    detail: String,
    progress: String,
    isCompleted: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(14.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    if (isCompleted) EnafActionBlue else EnafHeaderBorder,
                    CircleShape
                )
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(detail, color = EnafTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(progress, color = EnafActionBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
private fun RoadmapScreenPreview() {
    EnafTheme(darkTheme = true) {
        RoadmapScreen()
    }
}
