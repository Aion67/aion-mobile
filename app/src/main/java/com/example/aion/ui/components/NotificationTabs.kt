package com.example.aion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

@Composable
fun NotificationTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    unreadCount: Int,
    readCount: Int,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )
        },
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            text = {
                Text(
                    text = "Unread ($unreadCount)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        fontWeight = FontWeight.Medium,
                    )
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Tab(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            text = {
                Text(
                    text = "Read ($readCount)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = Variables.StaticBodyMediumSize,
                        lineHeight = Variables.StaticBodyMediumLineHeight,
                        fontWeight = FontWeight.Medium,
                    )
                )
            },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
