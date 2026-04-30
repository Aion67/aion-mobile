package com.example.aion.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Tab bar for the App Details screen.
 * Matches Figma node 7:1104.
 */
@Composable
fun AionTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        TabItem("Overview", Icons.Default.Info),
        TabItem("Settings", Icons.Default.Settings),
        TabItem("History", Icons.Default.Refresh)
    )

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.height(64.dp),
        containerColor = Variables.SchemesSurface,
        contentColor = Variables.PrimaryBrand,
        divider = {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Variables.NeutralGray.copy(alpha = 0.2f)
            )
        },
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = Variables.PrimaryBrand
                )
            }
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = tab.title,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = Variables.StaticTitleSmallSize,
                            lineHeight = Variables.StaticTitleSmallLineHeight,
                            letterSpacing = Variables.StaticTitleSmallTracking,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selectedContentColor = Variables.PrimaryBrand,
                unselectedContentColor = Variables.SchemesOnSurfaceVariant
            )
        }
    }
}

private data class TabItem(val title: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun AionTabsPreview() {
    AionTabs(
        selectedTabIndex = 0,
        onTabSelected = {}
    )
}
