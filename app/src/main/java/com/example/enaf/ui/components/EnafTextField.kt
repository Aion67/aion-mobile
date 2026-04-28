package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun EnafTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                color = EnafTextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
        }
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(EnafHeaderBg.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    color = EnafTextMuted,
                    fontSize = 14.sp
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            isError = errorText != null,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (errorText != null) EnafErrorRed else EnafActionBlue,
                unfocusedBorderColor = if (errorText != null) EnafErrorRed else EnafHeaderBorder,
                cursorColor = if (errorText != null) EnafErrorRed else EnafActionBlue,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 14.sp)
        )

        if (errorText != null) {
            Text(
                text = errorText,
                color = EnafErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }
}

@Preview
@Composable
fun EnafTextFieldPreview() {
    EnafTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(EnafDarkBg).padding(24.dp)) {
            EnafTextField(
                value = "",
                onValueChange = {},
                label = "Email Address",
                placeholder = "name@example.com",
                errorText = "Enter a valid email address."
            )
        }
    }
}
