package com.example.aion.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Revamped Stat Card with enhanced glassmorphism and pop animations.
 */
@Composable
fun AionStatCard(
    percentage: Int,
    label: String,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    var targetPercentage by remember { mutableIntStateOf(0) }
    val animatedPercentage by animateIntAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 1200),
        label = "StatValueAnimation"
    )

    LaunchedEffect(percentage) {
        targetPercentage = percentage
    }

    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
        blurRadius = 12.dp,
        glowColor = progressColor.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular badge indicating progress visually with animation
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(color = progressColor.copy(alpha = 0.15f), shape = CircleShape)
                    .border(1.dp, progressColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (animatedPercentage >= 0) "+$animatedPercentage%" else "$animatedPercentage%",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = Variables.StaticTitleSmallSize,
                        color = progressColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Dynamic analysis active",
                    style = MaterialTheme.typography.labelSmall.copy(
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
