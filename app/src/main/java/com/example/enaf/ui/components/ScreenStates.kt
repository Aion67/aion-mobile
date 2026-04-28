package com.example.enaf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafTextSecondary

@Composable
fun LoadingStateText(message: String) {
    Text(
        text = message,
        color = EnafTextSecondary,
        fontSize = 13.sp,
    )
}

@Composable
fun EmptyStateCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(EnafActionBlue.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .border(1.dp, EnafActionBlue.copy(alpha = 0.16f), RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Text(
            text = message,
            color = EnafTextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
        )
    }
}
