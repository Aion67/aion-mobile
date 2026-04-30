package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * A card showing daily usage history.
 */
@Composable
fun AionHistoryItem(
    date: String,
    day: String,
    usedTime: String,
    limitTime: String,
    percentage: Int,
    isExceeded: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Variables.SchemesSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$day, $date",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Variables.SchemesOnSurface
                    )
                    Text(
                        text = if (isExceeded) "Exceeded" else "Within Limit",
                        color = if (isExceeded) Variables.WarningRed else Variables.SuccessGreen,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Text(
                    text = "$percentage%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isExceeded) Variables.WarningRed else Variables.PrimaryBrand
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HistoryStat(label = "Used", value = usedTime)
                HistoryStat(label = "Limit", value = limitTime)
            }
        }
    }
}

@Composable
private fun HistoryStat(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Variables.SchemesOnSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Variables.SchemesOnSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AionHistoryItemPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AionHistoryItem(
            date = "01 Oct",
            day = "Mon",
            usedTime = "1h 45m",
            limitTime = "1h 30m",
            percentage = 116,
            isExceeded = true
        )
        AionHistoryItem(
            date = "02 Oct",
            day = "Tue",
            usedTime = "45m",
            limitTime = "1h 30m",
            percentage = 50,
            isExceeded = false
        )
    }
}
