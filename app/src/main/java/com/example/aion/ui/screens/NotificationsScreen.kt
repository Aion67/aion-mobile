package com.example.aion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aion.ui.components.AionTopAppBar
import com.example.aion.ui.components.NotificationTabs
import com.example.aion.ui.components.NotificationItem
import com.example.aion.ui.components.SortHeader
import com.example.aion.ui.theme.Variables
import com.example.aion.util.TimeUtils

import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.aion.ui.viewmodels.NotificationsViewModel
import com.example.aion.data.entities.NotificationEntity

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel(),
    avatarUri: String? = null,
    onAvatarClick: () -> Unit = {},
    onNotificationClick: (NotificationEntity) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    val unreadNotifications = uiState.notifications.filterNot { it.isRead }
    val readNotifications = uiState.notifications.filter { it.isRead }
    val visibleNotifications = if (selectedTabIndex == 0) unreadNotifications else readNotifications

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AionTopAppBar(
                title = "",
                onAvatarClick = onAvatarClick,
                avatarUri = avatarUri,
                containerColor = Color.Transparent,
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mark all as read") },
                                onClick = {
                                    showMenu = false
                                    viewModel.markAllAsRead()
                                }
                            )
                        }
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
            
            SortHeader(
                title = "Notifications",
                onSortClick = { /* Handle sort */ }
            )

            NotificationTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                unreadCount = unreadNotifications.size,
                readCount = readNotifications.size,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 140.dp), // Space for floating bar
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visibleNotifications) { notification ->
                    NotificationItem(
                        appName = notification.appPackageName ?: "System",
                        title = notification.title,
                        message = notification.message,
                        timestamp = TimeUtils.formatTimestamp(notification.timestamp),
                        modifier = Modifier.clickable { onNotificationClick(notification) },
                        iconRes = null
                    )
                }
            }
        }
    }
}
