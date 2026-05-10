package com.example.aion.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.theme.Variables

/**
 * Revamped AionTabs with glassy icon backgrounds and no bottom bar.
 */
@Composable
fun AionTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        TabItem("Overview", Icons.Default.Dashboard),
        TabItem("Settings", Icons.Default.Settings),
        TabItem("History", Icons.Default.History)
    )

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
        blurRadius = 12.dp,
        contentPadding = PaddingValues(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "TabContentColor"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .then(
                            if (isSelected) {
                                Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            } else Modifier
                        )
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            modifier = Modifier.size(20.dp),
                            tint = contentColor
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = tab.title,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = Variables.StaticLabelMediumSize,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                color = contentColor
                            )
                        )
                    }
                }
            }
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
