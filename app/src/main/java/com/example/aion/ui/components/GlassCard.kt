package com.example.aion.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.Shadow

/**
 * Enhanced GlassCard with lens refraction and dynamic shadows for visibility.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    blurRadius: Dp = 16.dp,
    glowColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    enableLens: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val blurPx = with(density) { blurRadius.toPx() }
    val isDark = isSystemInDarkTheme()
    
    // Dynamic border and shadow for light/dark mode visibility
    val defaultBorderColor = if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.1f)
    val resolvedBorderColor = borderColor ?: defaultBorderColor
    
    Box(
        modifier = modifier
            .clip(shape)
            .drawBackdrop(
                backdrop = emptyBackdrop(),
                shape = { shape },
                effects = { 
                    blur(blurPx)
                    if (enableLens) {
                        lens(refractionHeight = 8f, refractionAmount = 0.1f)
                    }
                },
                highlight = { Highlight(width = 0.5.dp, alpha = if (isDark) 0.3f else 0.15f) },
                shadow = {
                    if (glowColor != null) {
                        Shadow(
                            color = glowColor,
                            radius = 16.dp,
                            alpha = 0.4f
                        )
                    } else {
                        // Stronger shadow in light mode for separation
                        Shadow(
                            color = if (isDark) Color.Black.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.15f),
                            radius = if (isDark) 8.dp else 12.dp,
                            alpha = if (isDark) 0.5f else 0.3f
                        )
                    }
                },
                onDrawSurface = {
                    drawRect(color = containerColor)
                }
            )
            .border(borderWidth, resolvedBorderColor, shape)
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
