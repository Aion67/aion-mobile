package com.example.enaf.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.EnafActionBlue
import com.example.enaf.ui.theme.EnafTextMuted

@Composable
fun ScreenTitleBlock(
    title: String,
    subtitle: String? = null,
) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
    )
    if (!subtitle.isNullOrBlank()) {
        Text(
            text = subtitle,
            color = EnafTextMuted,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun SectionHeaderRow(
    title: String,
    trailingText: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        if (!trailingText.isNullOrBlank()) {
            Text(
                text = trailingText,
                color = EnafActionBlue,
                fontSize = 12.sp,
            )
        }
    }
}
