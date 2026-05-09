package com.example.aion.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * Header section for App Details screen showing icon and metadata.
 * Matches Figma node 7:523.
 */
@Composable
fun AppDetailsHeader(
    icon: Drawable?,
    lastOpened: String,
    notoriety: String,
    modifier: Modifier = Modifier
) {
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = Variables.StaticBodyMediumSize,
        lineHeight = Variables.StaticBodyMediumLineHeight,
        letterSpacing = Variables.StaticBodyMediumTracking,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface
    )

    val notorietyColor = when (notoriety) {
        "HARD" -> Color(0xFFB3261E) // Red
        "MODERATE" -> Color(0xFFE28905) // Orange/Amber
        else -> Variables.SuccessGreen // Green
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // App Icon
        Box(
            modifier = Modifier
                .size(117.dp)
                .clip(RoundedCornerShape(19.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Image(
                    bitmap = icon.toBitmap().asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

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
                Text(text = notoriety, style = textStyle.copy(color = notorietyColor), modifier = Modifier.height(29.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppDetailsHeaderPreview() {
    AppDetailsHeader(
        icon = null,
        lastOpened = "13:08",
        notoriety = "HARD"
    )
}
