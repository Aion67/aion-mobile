package com.example.aion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A reusable card with a custom glass switch for settings.
 */
@Composable
fun AionToggleCard(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 16.dp,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AionGlassSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionToggleCardPreview() {
    Box(Modifier.padding(16.dp)) {
        AionToggleCard(
            label = "App Tracking",
            description = "Enable or disable tracking for this app",
            checked = true,
            onCheckedChange = {}
        )
    }
}
