package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * An enhanced daily streak tracking component.
 * Displays a week's worth of streak data with visual feedback.
 */
@Composable
fun AionStreakBar(
    days: List<StreakDay>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            StreakDayItem(day = day)
        }
    }
}

@Composable
private fun StreakDayItem(
    day: StreakDay
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp) // Reduced from 64.dp
                .background(
                    color = if (day.isCompleted) Variables.PrimaryBrand else Variables.NeutralGray.copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .then(
                    if (day.isToday) {
                        Modifier.border(2.dp, Variables.PrimaryBrand, CircleShape)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp) // Reduced from 54.dp
                    .background(
                        color = if (day.isCompleted) Color.White else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.dateNumber,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp, // Reduced from Variables.StaticTitleLargeSize
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (day.isCompleted) Color.Black else Variables.SchemesOnSurface.copy(alpha = 0.5f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = day.dayName,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp, // Reduced from Variables.StaticBodyMediumSize
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (day.isToday) Variables.PrimaryBrand else Variables.SchemesOnSurface
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
