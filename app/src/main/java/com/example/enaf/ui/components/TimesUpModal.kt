package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.enaf.ui.theme.*

@Composable
fun TimesUpModal(
    onBlockApp: () -> Unit = {},
    onExtend: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)) // Dim background
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Atmospheric Gradient Background for the Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(532.dp)
                    .blur(40.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                EnafErrorRed.copy(alpha = 0.2f),
                                EnafDarkBg.copy(alpha = 0.5f)
                            )
                        ),
                        RoundedCornerShape(24.dp)
                    )
            )

            // Main Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(532.dp)
                    .background(EnafCardBg, RoundedCornerShape(24.dp))
                    .border(1.dp, EnafBorder, RoundedCornerShape(24.dp))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Bell Icon (Using Warning as fallback)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(EnafRedSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = EnafErrorRed
                    )
                }

                // Headline
                Text(
                    text = "Time's Up!",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // Message
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "You've reached your daily limit for Instagram.",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Every extra minute is a step away from your goals. Take control of your focus.",
                        color = EnafTextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }

                // Actions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnafButton(
                        text = "Block App",
                        onClick = onBlockApp,
                        containerColor = EnafErrorRed
                    )
                    
                    Button(
                        onClick = onExtend,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EnafHeaderBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Extend 5 Minutes", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close", color = EnafTextMuted, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TimesUpModalPreview() {
    EnafTheme(darkTheme = true) {
        TimesUpModal()
    }
}
