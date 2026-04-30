package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * A card for an individual notification.
 * Matches Figma node 8:2055.
 */
@Composable
fun NotificationItem(
    appName: String,
    title: String,
    message: String,
    timestamp: String,
    iconRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(77.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // App Icon
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = appName,
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(19.dp)),
            contentScale = ContentScale.FillBounds
        )

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = Variables.StaticTitleLargeSize,
                    lineHeight = Variables.StaticTitleLargeLineHeight,
                    color = Variables.SchemesOnSurface,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1
            )
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        color = Variables.SchemesOnSurfaceVariant
                    ),
                    maxLines = 1
                )
                
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = Variables.StaticBodySmallSize,
                        lineHeight = Variables.StaticBodySmallLineHeight,
                        color = Variables.SchemesOnSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
    NotificationItem(
        appName = "Instagram",
        title = "Instagram Limit Reached",
        message = "Notification from instagram",
        timestamp = "12:30",
        iconRes = R.drawable.tiktok
    )
}
