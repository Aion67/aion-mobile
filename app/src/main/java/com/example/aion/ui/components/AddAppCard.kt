package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * A card for the "Add Apps" screen.
 * Matches Figma node 8:1229 and variations.
 */
@Composable
fun AddAppCard(
    appName: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    usedTime: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = appName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = Variables.StaticTitleLargeSize,
                    lineHeight = Variables.StaticTitleLargeLineHeight,
                    color = Variables.SchemesOnSurface,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1
            )

            if (progress != null) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Variables.PrimaryBrand,
                    trackColor = Variables.PrimaryBrand.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            } else if (usedTime != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Used: $usedTime",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Variables.SchemesOnSurfaceVariant
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddAppCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddAppCard(
            appName = "Instagram",
            iconRes = R.drawable.tiktok,
            progress = 0.6f
        )
        AddAppCard(
            appName = "TikTok",
            iconRes = R.drawable.tiktok,
            usedTime = "12h 35m"
        )
        AddAppCard(
            appName = "Reddit",
            iconRes = R.drawable.tiktok
        )
    }
}
