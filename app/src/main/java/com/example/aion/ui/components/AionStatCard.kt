package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Compact stat card used on Home screen (e.g., 30% component from design).
 * Shows a percentage, label, and a small circular progress indicator-like badge.
 */
@Composable
fun AionStatCard(
    percentage: Int,
    label: String,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular badge indicating progress visually
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(color = progressColor.copy(alpha = 0.12f), shape = RoundedCornerShape(26.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = Variables.StaticTitleSmallSize,
                        lineHeight = Variables.StaticTitleSmallLineHeight,
                        color = progressColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionStatCardPreview() {
    AionStatCard(percentage = 30, label = "Focus Time Today")
}
