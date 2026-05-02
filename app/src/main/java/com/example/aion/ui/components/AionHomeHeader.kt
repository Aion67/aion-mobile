package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
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
 * Header for Home screen showing avatar, greeting and a small summary row.
 */
@Composable
fun AionHomeHeader(
    userName: String,
    improvementPercentage: Float,
    avatarRes: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = Variables.StaticLabelLargeSize,
                    lineHeight = Variables.StaticLabelLargeLineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = Variables.StaticHeadlineSmallSize,
                    lineHeight = Variables.StaticHeadlineSmallLineHeight,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            val improvementText = if (improvementPercentage >= 0) {
                "+${(improvementPercentage * 100).toInt()}% Improvement"
            } else {
                "${(improvementPercentage * 100).toInt()}% Improvement"
            }
            val improvementColor = if (improvementPercentage >= 0) {
                Variables.SuccessGreen
            } else {
                Variables.WarningRed
            }

            Text(
                text = improvementText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = Variables.StaticLabelMediumSize,
                    lineHeight = Variables.StaticLabelMediumLineHeight,
                    color = improvementColor,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        if (avatarRes != null) {
            IconButton(onClick = { /* open profile */ }) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp)) { /* spacer */ }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionHomeHeaderPreview() {
    AionHomeHeader(userName = "Alex", improvementPercentage = 0.05f, avatarRes = R.drawable.tiktok)
}
