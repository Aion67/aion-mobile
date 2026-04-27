package com.example.enaf.ui.screens.achievements

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
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafCardBg
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafHeaderBorder
import com.example.enaf.ui.theme.EnafTextMuted
import com.example.enaf.ui.theme.EnafTextSecondary
import com.example.enaf.ui.theme.EnafTheme

@Composable
fun AchievementsScreen(
    modifier: Modifier = Modifier,
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
                Text(
                    text = "Achievements",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text("6 / 20 unlocked", color = EnafTextMuted, fontSize = 13.sp)
            }

            item {
                BadgeRow(title = "3-Day Blackout", detail = "No social app limit breaks for 3 days", unlocked = true)
            }
            item {
                BadgeRow(title = "Night Discipline", detail = "No scrolling after 10 PM for 5 days", unlocked = true)
            }
            item {
                BadgeRow(title = "Diamond Mind", detail = "Earn 100 diamonds", unlocked = false)
            }
            item {
                BadgeRow(title = "Legend Sprint", detail = "Reach top 10 in weekly leaderboard", unlocked = false)
            }
        }
    }
}

@Composable
private fun BadgeRow(
    title: String,
    detail: String,
    unlocked: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(12.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(12.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(detail, color = EnafTextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
        }
        Text(
            text = if (unlocked) "Unlocked" else "Locked",
            color = if (unlocked) EnafActionBlue else EnafTextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun AchievementsScreenPreview() {
    EnafTheme(darkTheme = true) {
        AchievementsScreen()
    }
}
