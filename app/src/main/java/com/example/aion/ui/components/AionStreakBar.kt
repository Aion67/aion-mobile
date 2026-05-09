package com.example.aion.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * Data model for a single day in the streak bar.
 */
data class StreakDay(
    val dayName: String,
    val dateNumber: String,
    val isCompleted: Boolean,
    val isToday: Boolean = false
)

/**
 * Revamped Streak Bar with glass bubbles and glowing "Today" effect.
 */
@Composable
fun AionStreakBar(
    days: List<StreakDay>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEachIndexed { index, day ->
                StreakDayItem(day = day, delay = index * 100)
            }
        }
    }
}

@Composable
private fun StreakDayItem(
    day: StreakDay,
    delay: Int
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "StreakPopAnimation"
    )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible = true
    }

    Column(
        modifier = Modifier.scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GlassCard(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            containerColor = if (day.isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) 
                             else Color.White.copy(alpha = 0.1f),
            borderColor = if (day.isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) 
                           else Color.White.copy(alpha = 0.2f),
            borderWidth = if (day.isToday) 2.dp else 1.dp,
            glowColor = if (day.isToday) MaterialTheme.colorScheme.primary else null,
            blurRadius = 8.dp,
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dateNumber,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (day.isCompleted) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = day.dayName,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 11.sp,
                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (day.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AionStreakBarPreview() {
    val sampleDays = listOf(
        StreakDay("Mon", "01", true),
        StreakDay("Tue", "02", true),
        StreakDay("Wed", "03", true, isToday = true),
        StreakDay("Thu", "04", false),
        StreakDay("Fri", "05", false),
        StreakDay("Sat", "06", false),
        StreakDay("Sun", "07", false)
    )
    AionStreakBar(days = sampleDays)
}
