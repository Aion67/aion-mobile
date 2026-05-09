package com.example.aion.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.AppDestinations
import com.example.aion.ui.components.GlassCard
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur

/**
 * Revamped Glass Bottom Bar with icon glow and scale animations.
 */
@Composable
fun GlassBottomBar(
    currentDestination: AppDestinations,
    onDestinationSelected: (AppDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(32.dp),
            containerColor = Color.Black.copy(alpha = 0.6f),
            borderColor = Color.White.copy(alpha = 0.15f),
            blurRadius = 32.dp,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppDestinations.entries.forEach { destination ->
                    val isSelected = destination == currentDestination
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val tint by animateColorAsState(
                        targetValue = if (isSelected) primaryColor else Color.Gray.copy(alpha = 0.7f),
                        label = "NavIconTint"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.15f else 1f,
                        label = "NavIconScale"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = { onDestinationSelected(destination) }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .scale(scale)
                                .clip(CircleShape)
                                .then(
                                    if (isSelected) {
                                        Modifier.drawBackdrop(
                                            backdrop = emptyBackdrop(),
                                            shape = { CircleShape },
                                            effects = { blur(12f) },
                                            onDrawSurface = {
                                                drawCircle(color = primaryColor.copy(alpha = 0.25f))
                                            }
                                        )
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                tint = tint,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        if (isSelected) {
                            Text(
                                text = destination.label,
                                color = primaryColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}
