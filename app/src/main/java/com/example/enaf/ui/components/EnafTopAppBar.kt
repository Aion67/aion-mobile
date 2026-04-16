package com.example.enaf.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enaf.ui.theme.*

@Composable
fun EnafTopAppBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        color = EnafHeaderBg
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Bottom Border
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = EnafHeaderBorder,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile and Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    // Profile Image with Border
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .size(32.dp)
                            .border(1.dp, EnafProfileBorder, CircleShape)
                            .padding(1.dp)
                            .clip(CircleShape)
                    ) {
                        // Using a placeholder icon
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // "Enaf" Logo Text
                    Text(
                        text = "Enaf",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp
                    )
                }

                // Notification Icon
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EnafTopAppBarPreview() {
    EnafTheme(darkTheme = true) {
        EnafTopAppBar()
    }
}
