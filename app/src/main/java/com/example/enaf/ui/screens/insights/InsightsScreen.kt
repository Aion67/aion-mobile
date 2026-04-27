package com.example.enaf.ui.screens.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.components.EnafTopAppBar
import com.example.enaf.ui.theme.*

@Composable
fun InsightsScreen(
    modifier: Modifier = Modifier,
    uiState: InsightsUiState = insightsPreviewState(),
    onEvent: (InsightsUiEvent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            EnafTopAppBar()
        },
        containerColor = EnafDarkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header & Tabs
            item {
                Column {
                    Text(
                        text = "Performance Insights",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Segmented Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .background(EnafHeaderBg, RoundedCornerShape(23.dp))
                            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(23.dp))
                            .padding(4.dp)
                    ) {
                        val tabs = listOf("Weekly", "Monthly", "Yearly")
                        tabs.forEachIndexed { index, title ->
                            val range = when (index) {
                                0 -> InsightsRange.WEEKLY
                                1 -> InsightsRange.MONTHLY
                                else -> InsightsRange.YEARLY
                            }
                            val selected = uiState.selectedRange == range
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selected) EnafActionBlue else Color.Transparent)
                                    .let { if (selected) it.padding(horizontal = 12.dp) else it }
                                    .clickable { onEvent(InsightsUiEvent.RangeSelected(range)) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    color = if (selected) Color.White else EnafTextSecondary,
                                    fontSize = 14.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.Transparent)
                                        .padding(horizontal = 2.dp),
                                )
                            }
                        }
                    }
                }
            }

            // Monthly Streak Hero
            item {
                if (uiState.isLoading) {
                    Text(
                        text = "Loading insights...",
                        color = EnafTextSecondary,
                        fontSize = 13.sp,
                    )
                } else {
                    MonthlyStreakCard(
                        streakDays = uiState.streakDays,
                        totalDaysLabel = uiState.totalDaysLabel,
                    )
                }
            }

            // Category Distribution
            item {
                CategoryDistributionCard(uiState.categories)
            }

            // Pattern Observations
            item {
                PatternObservationsList(uiState.observations)
            }
        }
    }
}

@Composable
fun MonthlyStreakCard(streakDays: Int, totalDaysLabel: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(191.dp)
    ) {
        // Hero background glow
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 20.dp)
                .blur(60.dp)
                .background(EnafActionBlue.copy(alpha = 0.2f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EnafCardBg, RoundedCornerShape(16.dp))
                .border(1.dp, EnafBorder, RoundedCornerShape(16.dp))
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "CONSISTENCY METRIC",
                    color = EnafActionBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Monthly Streak",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = streakDays.toString(),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = totalDaysLabel,
                    color = EnafTextSecondary,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Simplified Calendar dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(7) { i ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(
                                if (i < 4) EnafActionBlue else EnafProgressBg,
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDistributionCard(categories: List<InsightsCategoryUiModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafCardBg, RoundedCornerShape(16.dp))
            .border(1.dp, EnafBorder, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Text(
            text = "Category Distribution",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Simplified Donut Chart placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(12.dp, EnafActionBlue, CircleShape)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("72%", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                    Text("FOCUS", color = EnafTextMuted, fontSize = 10.sp)
                }
            }
            
            Spacer(modifier = Modifier.width(32.dp))
            
            // Legend
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                categories.forEach { category ->
                    LegendItem(category.label, Color(category.accentColor), category.percentageLabel)
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color, percentage: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp, 24.dp).background(color, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = percentage, color = EnafTextMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun PatternObservationsList(observations: List<InsightsObservationUiModel>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Pattern Observations",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        
        observations.forEach { observation ->
            ObservationItem(
                title = observation.title,
                description = observation.description,
                iconColor = Color(observation.accentColor),
            )
        }
    }
}

@Composable
fun ObservationItem(title: String, description: String, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafHeaderBg, RoundedCornerShape(12.dp))
            .border(1.dp, EnafHeaderBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp, 36.dp)
                .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = description, color = EnafTextSecondary, fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}

@Preview
@Composable
fun InsightsScreenPreview() {
    EnafTheme(darkTheme = true) {
        InsightsScreen()
    }
}
