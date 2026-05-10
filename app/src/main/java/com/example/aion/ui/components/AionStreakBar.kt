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
 * Refined Streak Bar with compact glass beads.
 */
@Composable
fun AionStreakBar(
    days: List<StreakDay>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 12.dp,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEachIndexed { index, day ->
                StreakDayItem(day = day, delay = index * 80)
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
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        GlassCard(
            modifier = Modifier.size(38.dp), // More compact
            shape = CircleShape,
            containerColor = if (day.isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.75f) 
                             else Color.White.copy(alpha = 0.08f),
            borderColor = if (day.isToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) 
                           else Color.White.copy(alpha = 0.15f),
            borderWidth = if (day.isToday) 2.dp else 0.5.dp,
            glowColor = if (day.isToday) MaterialTheme.colorScheme.primary else null,
            blurRadius = 8.dp,
            contentPadding = PaddingValues(0.dp),
            enableLens = true
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dateNumber,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (day.isCompleted) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = day.dayName,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,
                fontWeight = if (day.isToday) FontWeight.ExtraBold else FontWeight.Bold,
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
