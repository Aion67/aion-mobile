package com.example.aion.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.aion.ui.theme.Variables
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur

/**
 * Revamped Plan App Card with glassmorphism and long-press interaction.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanAppCard(
    modifier: Modifier = Modifier,
    appName: String = "TikTok",
    icon: Drawable? = null,
    iconRes: Int? = null,
    creditScore: String = "76.23",
    usedTime: String = "12h 35m",
    remainingTime: String = "12h 35m",
    progress: Float = 0.8f,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val haptic = LocalHapticFeedback.current
    val labelStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticLabelLargeSize,
        lineHeight = Variables.StaticLabelLargeLineHeight,
        fontFamily = Variables.TitleFontFamily,
        letterSpacing = Variables.StaticLabelLargeTracking,
        color = MaterialTheme.colorScheme.onSurface
    )

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = { onClick?.invoke() },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick?.invoke()
                }
            ),
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        blurRadius = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left Section: Icon and Credit Score
            Column(
                modifier = Modifier.width(64.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (icon != null) {
                        Image(
                            bitmap = icon.toBitmap().asImageBitmap(),
                            contentDescription = appName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    } else if (iconRes != null && iconRes != 0) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = appName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Text(
                    text = creditScore,
                    style = labelStyle.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Right Section: Title, Progress, and Time Stats
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = appName,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = Variables.StaticTitleMediumSize,
                        fontFamily = Variables.TitleFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Custom Glass Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .drawBackdrop(
                                backdrop = emptyBackdrop(),
                                shape = { CircleShape },
                                effects = { blur(8f) } // blurPx is expected here, or a value
                            )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    TimeRow(label = "Used:", value = usedTime, style = labelStyle)
                    TimeRow(label = "Remaining:", value = remainingTime, style = labelStyle)
                }
            }
        }
    }
}

@Composable
private fun TimeRow(label: String, value: String, style: androidx.compose.ui.text.TextStyle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = style.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurfaceVariant))
        Text(text = value, style = style.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.End)
    }
}

@Preview(showBackground = true)
@Composable
fun PlanAppCardPreview() {
    PlanAppCard()
}
