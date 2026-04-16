package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun GlobalLimitCard(
    modifier: Modifier = Modifier,
    initialLimitHours: Float = 2f
) {
    var limit by remember { mutableFloatStateOf(initialLimitHours) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        // Decorative Glow
        Box(
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-40).dp)
                .blur(40.dp)
                .background(EnafActionBlue.copy(alpha = 0.2f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EnafCardBg, RoundedCornerShape(12.dp))
                .padding(25.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Global Daily Limit",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Current limit is set to ",
                        color = EnafTextMuted,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${limit.toInt()}h ${( (limit % 1) * 60 ).toInt()}m",
                        color = EnafActionBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bell Icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(EnafHeaderBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = EnafActionBlue
                    )
                }
            }

            Column {
                Slider(
                    value = limit,
                    onValueChange = { limit = it },
                    valueRange = 0f..6f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = EnafActionBlue,
                        inactiveTrackColor = EnafProgressBg
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("0", "1h", "2h", "3h", "4h", "6h").forEach { label ->
                        Text(
                            text = label,
                            color = EnafTextLabel,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GlobalLimitCardPreview() {
    EnafTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(EnafDarkBg).padding(16.dp)) {
            GlobalLimitCard()
        }
    }
}
