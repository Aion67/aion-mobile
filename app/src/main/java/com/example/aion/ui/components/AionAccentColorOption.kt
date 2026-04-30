package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * A selectable accent color option represented as a circular swatch.
 * Used in the Theme customization screen for color selection.
 */
@Composable
fun AionAccentColorOption(
    color: Color,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
                .border(
                    width = if (isSelected) 3.dp else 2.dp,
                    color = if (isSelected) Variables.PrimaryBrand else Variables.SchemesOutline.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(22.dp)
                        .background(
                            color = Variables.SchemesSurface.copy(alpha = 0.18f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Variables.SchemesSurface,
                    )
                }
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = Variables.StaticBodySmallSize,
                lineHeight = Variables.StaticBodySmallLineHeight,
                color = Variables.SchemesOnSurface,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AionAccentColorOptionPreview() {
    Column(
        modifier = Modifier
            .background(Variables.SchemesSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AionAccentColorOption(
                color = Variables.PrimaryBrand,
                label = "Purple",
                isSelected = true,
                onClick = {}
            )

            AionAccentColorOption(
                color = Color(0xFFEC4899),
                label = "Pink",
                isSelected = false,
                onClick = {}
            )

            AionAccentColorOption(
                color = Color(0xFF10B981),
                label = "Green",
                isSelected = false,
                onClick = {}
            )

            AionAccentColorOption(
                color = Color(0xFFFF6B6B),
                label = "Red",
                isSelected = false,
                onClick = {}
            )
        }
    }
}
