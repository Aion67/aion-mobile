package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionHomeHeader
import com.example.aion.ui.components.AionStatCard
import com.example.aion.ui.theme.Variables
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.aion.ui.components.AionProgressGauge
import com.example.aion.ui.components.PlanAppCard
import com.example.aion.ui.components.AionTopAppBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
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
            items(listOf(
                Pair("Instagram", 0.8f),
                Pair("TikTok", 0.5f),
                Pair("YouTube", 0.2f)
            )) { item ->
                PlanAppCard(
                    appName = item.first,
                    progress = item.second
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