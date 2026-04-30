package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * Header section for App Details screen showing icon and metadata.
 * Matches Figma node 7:523.
 */
@Composable
fun AppDetailsHeader(
    iconRes: Int,
    lastOpened: String,
    dataUsage: String,
    notoriety: String,
    modifier: Modifier = Modifier,
    notorietyColor: Color = Color(0xFFB3261E) // M3/sys/light/error or custom red
) {
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticBodyMediumSize,
        lineHeight = Variables.StaticBodyMediumLineHeight,
        letterSpacing = Variables.StaticBodyMediumTracking,
        fontWeight = FontWeight.Normal,
        color = Color.Black
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // App Icon
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(117.dp)
                .clip(RoundedCornerShape(19.dp)),
            contentScale = ContentScale.Crop
        )

        // Metadata Table
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Labels
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 13.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(text = "Last opened:", style = textStyle, modifier = Modifier.height(29.dp))
                Text(text = "Data usage:", style = textStyle, modifier = Modifier.height(29.dp))
                Text(text = "Notoriety:", style = textStyle, modifier = Modifier.height(29.dp))
            }

            // Values
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp, horizontal = 13.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(text = lastOpened, style = textStyle, modifier = Modifier.height(29.dp))
                Text(text = dataUsage, style = textStyle, modifier = Modifier.height(29.dp))
                Text(text = notoriety, style = textStyle.copy(color = notorietyColor), modifier = Modifier.height(29.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsHeaderPreview() {
    AppDetailsHeader(
        iconRes = R.drawable.tiktok, // Placeholder
        lastOpened = "13:08",
        dataUsage = "34 MB",
        notoriety = "HARD"
    )
}
