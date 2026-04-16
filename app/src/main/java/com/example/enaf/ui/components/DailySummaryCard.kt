package com.example.enaf.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun DailySummaryCard(
    modifier: Modifier = Modifier,
    progress: Float = 0.8f,
    hoursReclaimed: Double = 3.5
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .background(EnafHeroCardBg, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x1A1E293B), RoundedCornerShape(12.dp))
            .padding(25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "DAILY FOCUS",
                color = EnafActionBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Steady\nProgress",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You've reclaimed $hoursReclaimed hours of your life today.",
                color = EnafTextMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Circular Progress with Glow
        Box(
            modifier = Modifier.size(128.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow Effect
            Canvas(modifier = Modifier.size(110.dp).blur(8.dp)) {
                drawArc(
                    color = EnafActionBlue.copy(alpha = 0.3f),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Main Progress Circle
            Canvas(modifier = Modifier.size(100.dp)) {
                // Background Track
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    style = Stroke(width = 8.dp.toPx())
                )
                // Progress Arc
                drawArc(
                    color = EnafActionBlue,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Center Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "COMPLETE",
                    color = EnafTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.5).sp
                )
            }
        }
    }
}

@Preview
@Composable
fun DailySummaryCardPreview() {
    EnafTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(EnafDarkBg).padding(16.dp)) {
            DailySummaryCard()
        }
    }
}
