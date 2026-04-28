package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun EnafButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = EnafActionBlue,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Button Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
                .offset(y = 4.dp)
                .blur(8.dp)
                .background(
                    containerColor.copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                )
        )

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun EnafButtonPreview() {
    EnafTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(EnafDarkBg).padding(24.dp)) {
            EnafButton(text = "Get Started", onClick = {})
        }
    }
}
