package com.example.aion.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * Header section for App Details screen showing icon and metadata.
 * Matches Figma node 7:523.
 */
@Composable
fun AppDetailsHeader(
    icon: Drawable?,
    lastOpened: String,
    notoriety: String,
    modifier: Modifier = Modifier
) {
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticBodyMediumSize,
        lineHeight = Variables.StaticBodyMediumLineHeight,
        letterSpacing = Variables.StaticBodyMediumTracking,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface
    )

    val notorietyColor = when (notoriety) {
        "HARD" -> Color(0xFFB3261E) // Red
        "MODERATE" -> Color(0xFFE28905) // Orange/Amber
        else -> Variables.SuccessGreen // Green
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 24.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Image(
                        bitmap = icon.toBitmap().asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Metadata Table
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Last opened:", style = textStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text(text = lastOpened, style = textStyle.copy(fontWeight = FontWeight.Bold))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Notoriety:", style = textStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    Text(text = notoriety, style = textStyle.copy(color = notorietyColor, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsHeaderPreview() {
    AppDetailsHeader(
        icon = null,
        lastOpened = "13:08",
        notoriety = "HARD"
    )
}
