package com.example.aion.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
 * A reusable animated gauge component with a gap at the base.
 * Progress fills up on start. Value text is perfectly centered.
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
    // Animatable ensures the "filling up" animation triggers on entry
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1500)
        )
    }

    Box(
        modifier = modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
            
            // 270 degree arc, starting at 135 (bottom-left) leaving a gap at the bottom
            val startAngle = 135f
            val sweepAngle = 270f

            val topLeftOffset = androidx.compose.ui.geometry.Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )

            // Background Track
            drawArc(
                color = trackColor.copy(alpha = 0.2f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = arcSize,
                topLeft = topLeftOffset
            )

            // Animated Progress
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

        // Main Value Text - Centered exactly in the middle of the gauge
        Text(
            text = valueText,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = Variables.TitleFontFamily,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Metric Text - Aligned to Bottom Center in the gap of the arch
        Text(
            text = metricText,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontFamily = Variables.BodyFontFamily,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
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
