package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.aion.ui.components.AionHomeHeader
import com.example.aion.ui.components.AionProgressGauge
import com.example.aion.ui.components.AionStatCard
import com.example.aion.ui.components.PlanAppCard
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.theme.Variables

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAppClick: (AppDetailSpec) -> Unit = {},
) {
    val homeApps = listOf(
        AppDetailSpec("Instagram", com.example.aion.R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.8f),
        AppDetailSpec("TikTok", com.example.aion.R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.5f),
        AppDetailSpec("YouTube", com.example.aion.R.drawable.tiktok, "76.23", "12h 35m", "1h 35m", 0.2f)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(title = "", avatarRes = com.example.aion.R.drawable.tiktok)
        },
        containerColor = Variables.SchemesSurface
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AionHomeHeader(userName = "Alex", avatarRes = com.example.aion.R.drawable.tiktok)
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AionProgressGauge(
                        progress = 0.7623f,
                        valueText = "76.23",
                        metricText = "Score",
                        modifier = Modifier.weight(1f)
                    )

                    AionProgressGauge(
                        progress = 0.45f,
                        valueText = "45%",
                        metricText = "Today",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                AionStatCard(percentage = 30, label = "Improvement from yesterday")
            }

            item {
                AionStatCard(percentage = -10, label = "Improvement from last week")
            }

            // App cards list (sample)
            items(homeApps) { app ->
                PlanAppCard(
                    appName = app.appName,
                    iconRes = app.iconRes,
                    creditScore = app.creditScore,
                    usedTime = app.usedTime,
                    remainingTime = app.remainingTime,
                    progress = app.progress,
                    onClick = { onAppClick(app) }
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}