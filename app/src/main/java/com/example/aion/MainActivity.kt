package com.example.aion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.aion.ui.screens.AccentSettingsScreen
import com.example.aion.ui.screens.AppDetailSpec
import com.example.aion.ui.screens.AppDetailsScreen
import com.example.aion.ui.screens.NotificationsScreen
import com.example.aion.ui.screens.PlanScreen
import com.example.aion.ui.screens.AddAppsScreen
import com.example.aion.ui.screens.SettingsScreen
import com.example.aion.ui.screens.SettingsDetailScreen
import com.example.aion.ui.screens.ThemeSettingsScreen
import com.example.aion.ui.screens.HomeScreen
import com.example.aion.ui.theme.AionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AionTheme {
                AionApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun AionApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var settingsDestination by rememberSaveable { mutableStateOf(SettingsDestinations.LIST) }
    var showAddApps by rememberSaveable { mutableStateOf(false) }
    var selectedAppDetail by remember { mutableStateOf<AppDetailSpec?>(null) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            for (destination in AppDestinations.values()) {
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = destination == currentDestination,
                    onClick = {
                        settingsDestination = SettingsDestinations.LIST
                        selectedAppDetail = null
                        currentDestination = destination
                        showAddApps = false // Reset sub-navigation when switching main tabs
                    }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                selectedAppDetail != null -> {
                    AppDetailsScreen(
                        app = selectedAppDetail!!,
                        onBack = { selectedAppDetail = null }
                    )
                }

                else -> {
                    when (currentDestination) {
                        AppDestinations.HOME -> HomeScreen(
                            onAppClick = { selectedAppDetail = it }
                        )

                        AppDestinations.PLAN -> {
                            if (showAddApps) {
                                AddAppsScreen(onNavigateBack = { showAddApps = false })
                            } else {
                                PlanScreen(
                                    onNavigateToAddApps = { showAddApps = true },
                                    onAppClick = { selectedAppDetail = it }
                                )
                            }
                        }

                        AppDestinations.NOTIFICATIONS -> NotificationsScreen()

                        AppDestinations.SETTINGS -> {
                            when (settingsDestination) {
                                SettingsDestinations.LIST -> SettingsScreen(
                                    onThemeClick = { settingsDestination = SettingsDestinations.THEME },
                                    onAccentClick = { settingsDestination = SettingsDestinations.ACCENT },
                                    onFaqClick = { settingsDestination = SettingsDestinations.FAQ },
                                    onFeedbackClick = { settingsDestination = SettingsDestinations.FEEDBACK },
                                    onPrivacyClick = { settingsDestination = SettingsDestinations.PRIVACY },
                                    onVersionClick = { settingsDestination = SettingsDestinations.VERSION }
                                )

                                SettingsDestinations.THEME -> ThemeSettingsScreen(
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )

                                SettingsDestinations.ACCENT -> AccentSettingsScreen(
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )

                                SettingsDestinations.FAQ -> SettingsDetailScreen(
                                    title = "FAQ",
                                    body = "Frequently asked questions will be listed here.",
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )

                                SettingsDestinations.FEEDBACK -> SettingsDetailScreen(
                                    title = "Feedback",
                                    body = "Share your feedback and suggestions here.",
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )

                                SettingsDestinations.PRIVACY -> SettingsDetailScreen(
                                    title = "Privacy Policy",
                                    body = "Privacy policy details will be shown here.",
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )

                                SettingsDestinations.VERSION -> SettingsDetailScreen(
                                    title = "Version",
                                    body = "Aion version 1.0.0",
                                    onBack = { settingsDestination = SettingsDestinations.LIST }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class SettingsDestinations {
    LIST,
    THEME,
    ACCENT,
    FAQ,
    FEEDBACK,
    PRIVACY,
    VERSION,
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    PLAN("Plan", Icons.AutoMirrored.Filled.List),
    NOTIFICATIONS("Notifications", Icons.Default.Notifications),
    SETTINGS("Settings", Icons.Default.Settings),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AionTheme {
        Greeting("Android")
    }
}