package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * A selectable theme option card showing a preview and label.
 * Used in the Theme selection screen.
 * Matches Figma theme selection cards.
 */
@Composable
fun AionThemeOption(
    label: String,
    previewColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(
                    color = previewColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) Variables.PrimaryBrand else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(
                            color = Variables.PrimaryBrand,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Variables.SchemesSurface
                    )
                }
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = Variables.StaticBodyLargeSize,
                lineHeight = Variables.StaticBodyLargeLineHeight,
                color = Variables.SchemesOnSurface,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AionThemeOptionPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Light mode (not selected)
        AionThemeOption(
            label = "Light mode",
            previewColor = Color(0xFFECE6F0),
            isSelected = false,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        // Dark mode (selected)
        AionThemeOption(
            label = "Dark mode",
            previewColor = Color(0xFF1D1B20),
            isSelected = true,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}
