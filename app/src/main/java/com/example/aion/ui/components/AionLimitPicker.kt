package com.example.aion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * Revamped Limit Picker with glass input fields.
 */
@Composable
fun AionLimitPicker(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onValueChange: (h: Int, m: Int, s: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        TimeInputField(
            value = hours,
            label = "Hours",
            range = 0..23,
            onValueChange = { onValueChange(it, minutes, seconds) },
            modifier = Modifier.weight(1f)
        )
        Text(
            ":", 
            fontSize = 24.sp, 
            fontWeight = FontWeight.Bold, 
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TimeInputField(
            value = minutes,
            label = "Min",
            range = 0..59,
            onValueChange = { onValueChange(hours, it, seconds) },
            modifier = Modifier.weight(1f)
        )
        Text(
            ":", 
            fontSize = 24.sp, 
            fontWeight = FontWeight.Bold, 
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TimeInputField(
            value = seconds,
            label = "Sec",
            range = 0..59,
            onValueChange = { onValueChange(hours, minutes, it) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimeInputField(
    value: Int,
    label: String,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        GlassCard(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            blurRadius = 12.dp,
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                BasicTextField(
                    value = value.toString().padStart(2, '0'),
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }.take(2)
                        if (filtered.isEmpty()) {
                            onValueChange(0)
                        } else {
                            val num = filtered.toInt()
                            if (num in range) {
                                onValueChange(num)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AionLimitPickerPreview() {
    var h by remember { mutableStateOf(1) }
    var m by remember { mutableStateOf(30) }
    var s by remember { mutableStateOf(0) }
    
    Box(Modifier.padding(16.dp)) {
        AionLimitPicker(hours = h, minutes = m, seconds = s, onValueChange = { newH, newM, newS ->
            h = newH
            m = newM
            s = newS
        })
    }
}
