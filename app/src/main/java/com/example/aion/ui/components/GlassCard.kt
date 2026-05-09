package com.example.aion.ui.components

import androidx.compose.foundation.border
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
import com.kyant.backdrop.highlight.Highlight
import com.kyant.backdrop.shadow.Shadow

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.25f),
    borderWidth: Dp = 1.dp,
    blurRadius: Dp = 16.dp,
    glowColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val blurPx = with(density) { blurRadius.toPx() }
    
    Box(
        modifier = modifier
            .clip(shape)
            .drawBackdrop(
                backdrop = emptyBackdrop(),
                shape = { shape },
                effects = { blur(blurPx) },
                highlight = { Highlight(width = 0.5.dp, alpha = 0.3f) },
                shadow = {
                    if (glowColor != null) {
                        Shadow(
                            color = glowColor,
                            radius = 16.dp,
                            alpha = 0.5f
                        )
                    } else {
                        Shadow(
                            color = Color.Black.copy(alpha = 0.2f),
                            radius = 8.dp
                        )
                    }
                },
                onDrawSurface = {
                    drawRect(color = containerColor)
                }
            )
            .border(borderWidth, borderColor, shape)
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
