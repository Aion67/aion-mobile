package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * A Material 3 Keyboard Time Input component.
 * Allows users to enter hours and minutes manually.
 */
@Composable
fun AionTimeInput(
    onCancel: () -> Unit,
    onConfirm: (Int, Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var hour by remember { mutableStateOf("20") }
    var minute by remember { mutableStateOf("00") }
    var isAm by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier.width(328.dp),
        shape = RoundedCornerShape(28.dp),
        color = Variables.SchemesSurfaceContainerHigh
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Enter time",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Variables.SchemesOnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                // Hour & Minute Input Group
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeInputField(
                        value = hour,
                        onValueChange = { if (it.length <= 2) hour = it },
                        label = "Hour",
                        isFocused = true
                    )
                    
                    Text(
                        text = ":",
                        fontSize = 57.sp,
                        color = Variables.SchemesOnSurface,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )

                    TimeInputField(
                        value = minute,
                        onValueChange = { if (it.length <= 2) minute = it },
                        label = "Minute",
                        isFocused = false
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // AM/PM Selector
                Column(
                    modifier = Modifier
                        .width(52.dp)
                        .height(72.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Variables.SchemesOutline, RoundedCornerShape(8.dp))
                ) {
                    PeriodButton(
                        text = "AM",
                        isSelected = isAm,
                        onClick = { isAm = true },
                        modifier = Modifier.weight(1f)
                    )
                    HorizontalDivider(color = Variables.SchemesOutline, thickness = 1.dp)
                    PeriodButton(
                        text = "PM",
                        isSelected = !isAm,
                        onClick = { isAm = false },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Switch to picker mode */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Switch to picker",
                        tint = Variables.SchemesOnSurface
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel", color = MaterialTheme.colorScheme.primary)
                    }
                    TextButton(onClick = { 
                        onConfirm(hour.toIntOrNull() ?: 0, minute.toIntOrNull() ?: 0, isAm) 
                    }) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isFocused: Boolean
) {
    Column(
        modifier = Modifier.width(96.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isFocused) Variables.SchemesPrimaryContainer 
                    else Variables.SchemesSurfaceContainerHighest
                )
                .then(
                    if (isFocused) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 45.sp,
                    textAlign = TextAlign.Center,
                    color = if (isFocused) Variables.SchemesOnPrimaryContainer else Variables.SchemesOnSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = Variables.SchemesOnSurfaceVariant,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

@Composable
private fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isSelected) Variables.SchemesTertiaryContainer 
                else Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Variables.SchemesOnTertiaryContainer else Variables.SchemesOnSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AionTimeInputPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        AionTimeInput(onCancel = {}, onConfirm = { h, m, am -> })
    }
}
