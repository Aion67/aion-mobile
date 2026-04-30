package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.NotificationItem
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables

data class NotificationData(
    val appName: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val iconRes: Int
)

val mockNotifications = listOf(
    NotificationData("Instagram", "Instagram Limit Reached", "Notification from instagram", "12:30", R.drawable.tiktok),
    NotificationData("Instagram", "Instagram Limit Reached", "Notification from instagram", "12:30", R.drawable.tiktok),
    NotificationData("Instagram", "Instagram Limit Reached", "Notification from instagram", "12:30", R.drawable.tiktok),
    NotificationData("Instagram", "Instagram Limit Reached", "Notification from instagram", "12:30", R.drawable.tiktok),
    NotificationData("TikTok", "TikTok Limit Reached", "Notification from TikTok", "12:30", R.drawable.tiktok)
)

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onAvatarClick = onAvatarClick
            )
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
                title = "Notifications",
                onSortClick = { /* Handle sort */ }
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(mockNotifications) { notification ->
                    NotificationItem(
                        appName = notification.appName,
                        title = notification.title,
                        message = notification.message,
                        timestamp = notification.timestamp,
                        iconRes = notification.iconRes
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
