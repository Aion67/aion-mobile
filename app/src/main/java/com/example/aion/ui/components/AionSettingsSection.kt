package com.example.aion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildFriendly
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * A section header for grouping settings rows.
 * Used for sections like "General", "Custom", "About" in the Settings screen.
 * Matches Figma node 9:6554.
 */
@Composable
fun AionSettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = Variables.StaticTitleMediumSize,
                lineHeight = Variables.StaticTitleMediumLineHeight,
                letterSpacing = Variables.StaticTitleMediumTracking,
                fontWeight = FontWeight.Medium,
                color = Variables.SchemesOnSurface
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun AionSettingsSectionPreview() {
    Column {
        AionSettingsSection(
            title = "General",
            content = {
                AionSettingsRow(
                    title = "Notifications",
                    subtitle = "Turn on notifications",
                    leadingIcon = Icons.Filled.Notifications,
                    checked = true,
                    onCheckedChange = {}
                )
                AionSettingsRow(
                    title = "Display over other Apps",
                    subtitle = "Display over tracked apps when time is...",
                    leadingIcon = Icons.Filled.Notifications,
                    checked = true,
                    onCheckedChange = {}
                )
            }
        )
        AionSettingsSection(
            title = "About",
            content = {
                AionSettingsRow(
                    title = "FAQ",
                    leadingIcon = Icons.Filled.ChildFriendly,
                    onClick = {}
                )
            }
        )
    }
}

