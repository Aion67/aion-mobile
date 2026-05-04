package com.example.aion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * A header component for the Plan screen showing the section title and a sort option.
 * Matches Figma node 6:3356.
 */
@Composable
fun SortHeader(
    modifier: Modifier = Modifier,
    title: String = "Apps",
    onSortClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Side: Title (e.g., "Apps")
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = Variables.StaticHeadlineSmallSize,
                lineHeight = Variables.StaticHeadlineSmallLineHeight,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        // Right Side: Sort by + Sort Icon
        Row(
            modifier = Modifier
                .clickable(onClick = onSortClick)
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = Variables.StaticLabelMediumSize,
                    lineHeight = Variables.StaticLabelMediumLineHeight,
                    letterSpacing = Variables.StaticLabelMediumTracking,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort Icon",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SortHeaderPreview() {
    SortHeader()
}
