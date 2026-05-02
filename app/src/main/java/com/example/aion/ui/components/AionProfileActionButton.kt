package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AionProfileActionButton(
    avatarRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        shape = CircleShape,
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Open profile",
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Crop,
        )
    }
}