package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import com.example.aion.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import android.graphics.drawable.Drawable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap

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
    onDeleteClick: (() -> Unit)? = null,
) {
    val labelStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticLabelLargeSize,
        lineHeight = Variables.StaticLabelLargeLineHeight,
        fontFamily = Variables.TitleFontFamily,
        letterSpacing = Variables.StaticLabelLargeTracking,
        color = MaterialTheme.colorScheme.onSurface
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(103.dp)
            .padding(horizontal = 16.dp)
            .let { rowModifier ->
                if (onClick != null) {
                    rowModifier.clickable(onClick = onClick)
                } else {
                    rowModifier
                }
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
    ) {
        // Left Section: Icon and Credit Score
        Column(
            modifier = Modifier
                .width(76.dp)
                .height(103.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Text(
                text = creditScore,
                style = labelStyle.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.width(37.dp).height(20.dp)
            )
        }

        // Right Section: Title, Progress, and Time Stats
        Column(
            modifier = Modifier
                .weight(1f)
                .height(103.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = appName,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = Variables.StaticTitleLargeSize,
                        lineHeight = Variables.StaticTitleLargeLineHeight,
                        fontFamily = Variables.TitleFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier.weight(1f).height(28.dp)
                )

                if (onDeleteClick != null) {
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove App",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            Column(
                modifier = Modifier.fillMaxWidth().height(49.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                TimeRow(label = "Used:", value = usedTime, style = labelStyle)
                TimeRow(label = "Remaining:", value = remainingTime, style = labelStyle)
            }
        }
    }
}

@Composable
private fun TimeRow(label: String, value: String, style: androidx.compose.ui.text.TextStyle) {
    Row(
        modifier = Modifier.fillMaxWidth().height(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = style.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant))
        Text(text = value, style = style.copy(fontWeight = FontWeight.SemiBold), textAlign = TextAlign.End)
    }
}

@Preview(showBackground = true)
@Composable
fun PlanAppCardPreview() {
    PlanAppCard()
}
