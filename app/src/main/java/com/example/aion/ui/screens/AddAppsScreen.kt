package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.components.AddAppCard
import com.example.aion.ui.components.AionSearchBar
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables

data class AppItem(
    val name: String,
    val iconRes: Int,
    val progress: Float? = null,
    val usedTime: String? = null
)

val mockApps = listOf(
    AppItem("Instagram", R.drawable.tiktok, progress = 0.8f),
    AppItem("TikTok", R.drawable.tiktok, usedTime = "12h 35m"),
    AppItem("Reddit", R.drawable.tiktok, usedTime = "12h 35m"),
    AppItem("Snapchat", R.drawable.tiktok, progress = 0.9f),
    AppItem("Facebook", R.drawable.tiktok, usedTime = "12h 35m"),
    AppItem("YouTube", R.drawable.tiktok, usedTime = "12h 35m"),
    AppItem("X", R.drawable.tiktok, usedTime = "12h 35m"),
    AppItem("Pinterest", R.drawable.tiktok),
    AppItem("LinkedIn", R.drawable.tiktok)
)

@Composable
fun AddAppsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredApps = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            mockApps
        } else {
            mockApps.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                AionTopAppBar(
                    title = "",
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onLeadingClick = onNavigateBack
                )
                AionSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onClear = { searchQuery = "" }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Variables.SchemesSurface)
        ) {
            SortHeader(
                title = "Apps",
                onSortClick = { /* Handle sort */ }
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredApps) { app ->
                    AddAppCard(
                        appName = app.name,
                        iconRes = app.iconRes,
                        progress = app.progress,
                        usedTime = app.usedTime
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAppsScreenPreview() {
    AddAppsScreen()
}
