package com.example.aion.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * Revamped Animated Gauge with liquid glow and glass container.
 */
@Composable
fun AionProgressGauge(
    progress: Float,
    valueText: String,
    metricText: String,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    }

    GlassCard(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
        blurRadius = 16.dp,
        glowColor = progressColor.copy(alpha = 0.1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                val strokeWidth = 14.dp.toPx()
                val diameter = size.minDimension - strokeWidth - 10.dp.toPx()
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                
                val startAngle = 135f
                val sweepAngle = 270f

                val topLeftOffset = androidx.compose.ui.geometry.Offset(
                    (size.width - diameter) / 2,
                    (size.height - diameter) / 2
                )

                // Background Track
                drawArc(
                    color = trackColor.copy(alpha = 0.15f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    size = arcSize,
                    topLeft = topLeftOffset
                )

                // Outer Glow for Progress
                drawArc(
                    color = progressColor.copy(alpha = 0.25f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * animatedProgress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth + 8.dp.toPx(), cap = StrokeCap.Round),
                    size = arcSize,
                    topLeft = topLeftOffset
                )

                // Main Progress Arc
                drawArc(
                    color = progressColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * animatedProgress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    size = arcSize,
                    topLeft = topLeftOffset
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = valueText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = Variables.TitleFontFamily
                )
                
                Text(
                    text = metricText,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontFamily = Variables.BodyFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionProgressGaugePreview() {
    Box(Modifier.padding(24.dp)) {
        AionProgressGauge(
            progress = 0.7623f,
            valueText = "76.23",
            metricText = "Credits"
        )
    }
}
