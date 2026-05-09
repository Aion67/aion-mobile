package com.example.aion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * Revamped Keyboard Time Input with glass components.
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

    GlassCard(
        modifier = modifier.width(340.dp),
        shape = RoundedCornerShape(32.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 24.dp,
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Enter time",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
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
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 24.dp)
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
                        .width(56.dp)
                        .height(84.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    PeriodButton(
                        text = "AM",
                        isSelected = isAm,
                        onClick = { isAm = true },
                        modifier = Modifier.weight(1f)
                    )
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
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }
                    AionFilledButton(
                        text = "OK",
                        onClick = { 
                            onConfirm(hour.toIntOrNull() ?: 0, minute.toIntOrNull() ?: 0, isAm) 
                        },
                        modifier = Modifier.width(80.dp)
                    )
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
        modifier = Modifier.width(88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) 
                             else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            borderColor = if (isFocused) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
            blurRadius = 12.dp,
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
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
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) 
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
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
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
