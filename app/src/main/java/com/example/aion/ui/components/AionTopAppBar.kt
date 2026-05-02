package com.example.aion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
// use Icons.AutoMirrored.Filled.ArrowBack via Icons import
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.R
import com.example.aion.ui.theme.Variables

/**
 * A reusable Top App Bar for Aion.
 * Refactored to support profile icon on left and actions on right.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AionTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    leadingIcon: ImageVector? = null,
    onLeadingClick: () -> Unit = {},
    avatarRes: Int? = null,
    onAvatarClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = Variables.StaticTitleLargeSize,
                    lineHeight = Variables.StaticTitleLargeLineHeight,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        navigationIcon = {
            if (avatarRes != null) {
                IconButton(onClick = onAvatarClick) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            } else if (leadingIcon != null) {
                IconButton(onClick = onLeadingClick) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AionTopAppBarPreview() {
    AionTopAppBar(
        title = "Plan",
        avatarRes = R.drawable.tiktok,
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AionTopAppBarDetailsPreview() {
    AionTopAppBar(
        title = "App Details",
        leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onLeadingClick = {}
    )
}
