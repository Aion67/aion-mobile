package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun MotivationalCard(
    modifier: Modifier = Modifier,
    onTakeActionClick: () -> Unit = {}
) {
    var isFearReminderEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Outer Glow/Blur effect (simplified for Compose)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 2.dp)
                .blur(8.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            EnafErrorRed.copy(alpha = 0.2f),
                            Color(0xFFF97316).copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Main Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(EnafCardBg, RoundedCornerShape(12.dp))
                .border(1.dp, EnafBorder, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Beat Procrastination",
                        color = EnafTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The cost of inaction is hidden but heavy.",
                        color = EnafTextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                // Warning Icon Container
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(EnafRedSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        modifier = Modifier.size(20.dp),
                        tint = EnafErrorRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quote Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EnafOverlay, RoundedCornerShape(8.dp))
                    .border(1.dp, EnafRedSoft, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "\"In one year, you'll wish you had started today. Every scroll is a minute of your potential gone forever.\"",
                    color = EnafTextItalic,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "FEAR REMINDER",
                        color = EnafErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = isFearReminderEnabled,
                        onCheckedChange = { isFearReminderEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = EnafErrorRed,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                Button(
                    onClick = onTakeActionClick,
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = EnafActionBlue),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Take Action",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MotivationalCardPreview() {
    EnafTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(EnafDarkBg)) {
            MotivationalCard()
        }
    }
}
