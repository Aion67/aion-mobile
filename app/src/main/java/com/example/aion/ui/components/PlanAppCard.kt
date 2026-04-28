package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
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

@Composable
fun PlanAppCard(
    modifier: Modifier = Modifier,
    appName: String = "TikTok",
    iconRes: Int = R.drawable.tiktok,
    creditScore: String = "76.23",
    usedTime: String = "12h 35m",
    remainingTime: String = "12h 35m",
    progress: Float = 0.8f,
) {
    val labelStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticLabelLargeSize,
        lineHeight = Variables.StaticLabelLargeLineHeight,
        fontFamily = Variables.TitleFontFamily,
        letterSpacing = Variables.StaticLabelLargeTracking,
        color = Color(0xFF000000)
    )

    Row(
        modifier = modifier
            .width(380.dp)
            .height(103.dp),
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
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = appName,
                modifier = Modifier
                    .width(76.dp)
                    .height(76.dp),
                contentScale = ContentScale.FillBounds
            )
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
            Text(
                text = appName,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = Variables.StaticTitleLargeSize,
                    lineHeight = Variables.StaticTitleLargeLineHeight,
                    fontFamily = Variables.TitleFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Variables.SchemesOnSurface,
                ),
                modifier = Modifier.fillMaxWidth().height(28.dp)
            )

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
        Text(text = label, style = style.copy(fontWeight = FontWeight.SemiBold))
        Text(text = value, style = style.copy(fontWeight = FontWeight.SemiBold), textAlign = TextAlign.End)
    }
}

@Preview(showBackground = true)
@Composable
fun PlanAppCardPreview() {
    PlanAppCard()
}
