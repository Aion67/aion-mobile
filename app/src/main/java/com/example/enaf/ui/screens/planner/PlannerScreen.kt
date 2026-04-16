package com.example.enaf.ui.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.*
import com.example.enaf.ui.theme.*

@Composable
fun PlannerScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

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
            // Screen Title
            item {
                Column {
                    Text(
                        text = "Screen Time Planner",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Optimize your focus and digital wellbeing.",
                        color = EnafTextMuted,
                        fontSize = 14.sp
                    )
                }
            }

            // Global Limit Card
            item {
                GlobalLimitCard()
            }

            // Search Bar
            item {
                EnafTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search apps...",
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = EnafTextMuted)
                    }
                )
            }

            // Individual App Limits Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Individual App Limits",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sort by Use",
                        color = EnafActionBlue,
                        fontSize = 12.sp
                    )
                }
            }

            // Bento Style List (Using AppUsageItem)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppUsageItem(
                        appName = "TikTok",
                        usedTime = "+45m used",
                        limitTime = "Limit: 1h",
                        remainingTime = "15m remaining",
                        progress = 0.75f,
                        accentColor = Color(0xFF00F2EA)
                    )
                    AppUsageItem(
                        appName = "YouTube",
                        usedTime = "+1h 20m",
                        limitTime = "Limit: 2h",
                        remainingTime = "40m remaining",
                        progress = 0.66f,
                        accentColor = Color(0xFFFF0000)
                    )
                    AppUsageItem(
                        appName = "Instagram",
                        usedTime = "+12m used",
                        limitTime = "Limit: 45m",
                        remainingTime = "13m remaining",
                        progress = 0.72f,
                        accentColor = EnafPink
                    )
                }
            }
            
            // Usage Insight Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EnafActionBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .border(1.dp, EnafActionBlue.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(24.dp).background(EnafActionBlue.copy(alpha = 0.2f), RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Your social media usage is down 12% from last week. Keep it up!",
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
