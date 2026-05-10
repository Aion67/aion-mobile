package com.example.aion.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

/**
 * Custom Glass Menu for Sorting
 */
@Composable
fun AionGlassMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    if (expanded) {
        Popup(
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            Box(modifier = modifier.padding(top = 4.dp)) {
                GlassCard(
                    modifier = Modifier.width(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    blurRadius = 24.dp,
                    contentPadding = PaddingValues(8.dp),
                    glowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    enableLens = true
                ) {
                    Column {
                        items.forEach { (label, onClick) ->
                            TextButton(
                                onClick = {
                                    onClick()
                                    onDismissRequest()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
