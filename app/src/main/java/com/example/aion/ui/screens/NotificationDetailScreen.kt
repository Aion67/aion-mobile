package com.example.aion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.theme.Variables
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.example.aion.R

@Composable
fun NotificationDetailScreen(
    notification: NotificationSpec,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(title = "Notification", leadingIcon = Icons.AutoMirrored.Filled.ArrowBack, onLeadingClick = onBack)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = notification.iconRes),
                contentDescription = notification.appName,
                modifier = Modifier.padding(bottom = 12.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                text = "From: ${notification.appName} • ${notification.timestamp}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun NotificationDetailScreenPreview() {
    val sampleNotification = NotificationSpec(
        appName = "YouTube",
        title = "New Video Uploaded",
        message = "Your favorite channel just uploaded a new video. Check it out now!",
        timestamp = "2 hours ago",
        iconRes = R.drawable.tiktok
    )
    NotificationDetailScreen(notification = sampleNotification, onBack = {})
}
