package com.example.aion.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * A card for the "Add Apps" screen.
 * Matches Figma node 8:1229 and variations.
 */
@Composable
fun AddAppCard(
    appName: String,
    modifier: Modifier = Modifier,
    icon: Drawable? = null,
    progress: Float? = null,
    usedTime: String? = null,
    isTracked: Boolean = false,
    onToggleTracking: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .clickable { onToggleTracking() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Icon
        if (icon != null) {
            Image(
                bitmap = icon.toBitmap().asImageBitmap(),
                contentDescription = appName,
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(19.dp)),
                contentScale = ContentScale.FillBounds
            )
        } else {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(Variables.SchemesSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // Fallback icon
            }
        }

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

        // Checkbox for tracking status
        Checkbox(
            checked = isTracked,
            onCheckedChange = { onToggleTracking() }
        )
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
            progress = 0.6f
        )
        AddAppCard(
            appName = "TikTok",
            usedTime = "12h 35m"
        )
        AddAppCard(
            appName = "Reddit"
        )
    }
}
