package com.example.aion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aion.ui.theme.Variables

/**
 * A component to specify duration in hours, minutes, and seconds.
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeInputField(
            value = hours,
            label = "HH",
            range = 0..23,
            onValueChange = { onValueChange(it, minutes, seconds) },
            modifier = Modifier.weight(1f)
        )
        Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        TimeInputField(
            value = minutes,
            label = "MM",
            range = 0..59,
            onValueChange = { onValueChange(hours, it, seconds) },
            modifier = Modifier.weight(1f)
        )
        Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        TimeInputField(
            value = seconds,
            label = "SS",
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
    OutlinedTextField(
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
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Variables.PrimaryBrand,
            unfocusedBorderColor = Variables.NeutralGray
        )
    )
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
