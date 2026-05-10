package com.example.aion.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Custom Glass Switch for Aion.
 */
@Composable
fun AionGlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "ThumbOffset"
    )
    
    val trackColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) 
                      else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        label = "TrackColor"
    )

    Box(
        modifier = modifier
            .width(52.dp)
            .height(28.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        GlassCard(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp),
            shape = CircleShape,
            containerColor = if (checked) Color.White else Color.White.copy(alpha = 0.8f),
            borderColor = if (checked) Color.White else Color.White.copy(alpha = 0.5f),
            blurRadius = 4.dp,
            contentPadding = PaddingValues(0.dp),
            enableLens = true,
            glowColor = if (checked) MaterialTheme.colorScheme.primary else null
        ) {
            // Empty content for thumb
        }
    }
}
