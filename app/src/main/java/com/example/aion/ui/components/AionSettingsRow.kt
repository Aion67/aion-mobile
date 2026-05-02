package com.example.aion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Reusable settings row for the general Settings screen.
 * Matches the Figma row pattern with a leading icon, title, optional subtitle,
 * and either a trailing switch or a navigation affordance.
 */
@Composable
fun AionSettingsRow(
    title: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val rowModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
    } else {
        modifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier
            .heightIn(min = 72.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = Variables.StaticBodyLargeSize,
                    lineHeight = Variables.StaticBodyLargeLineHeight,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 1
            )

            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2
                )
            }
        }

        when {
            checked != null && onCheckedChange != null -> {
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
                        uncheckedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
                    )
                )
            }

            onClick != null -> {
                IconButton(onClick = onClick, enabled = enabled) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open $title",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionSettingsRowPreview() {
    Column {
        AionSettingsRow(
            title = "Notifications",
            subtitle = "Turn on notifications",
            leadingIcon = Icons.Outlined.Notifications,
            checked = true,
            onCheckedChange = {}
        )
        AionSettingsRow(
            title = "Theme",
            subtitle = "Choose your preferred theme and background",
            leadingIcon = Icons.Outlined.Notifications,
            onClick = {}
        )
    }
}
