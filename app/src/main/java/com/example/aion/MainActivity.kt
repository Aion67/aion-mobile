package com.example.aion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aion.data.manager.WorkScheduler
import com.example.aion.ui.navigation.Screen
import com.example.aion.ui.screens.*
import com.example.aion.ui.theme.AionTheme
import com.example.aion.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        workScheduler.scheduleUsageSync()

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.uiState.collectAsState()

            AionTheme(
                themeMode = settingsUiState.theme,
                accentColor = settingsUiState.accentColor
            ) {
                AionApp()
            }
        }
    }
}

@Composable
fun AionApp() {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val onboardingViewModel: com.example.aion.ui.viewmodels.OnboardingViewModel = hiltViewModel()
    val onboardingState by onboardingViewModel.uiState.collectAsState()

    val startDestination = if (onboardingState.isCompleted) Screen.Home.route else Screen.Onboarding.route

    // Don't show navigation bar on onboarding
    val showNavBar = onboardingState.isCompleted

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            if (showNavBar) {
                for (destination in AppDestinations.entries) {
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
                            currentDestination = destination
                            when (destination) {
                                AppDestinations.HOME -> navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                                AppDestinations.PLAN -> navController.navigate(Screen.Plan.route)
                                AppDestinations.NOTIFICATIONS -> navController.navigate(Screen.Notifications.route)
                                AppDestinations.SETTINGS -> navController.navigate(Screen.Settings.route)
                            }
                        }
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(Screen.Onboarding.route) {
                    OnboardingScreen(
                        onFinish = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Screen.Home.route) {
                    LaunchedEffect(Unit) {
                        currentDestination = AppDestinations.HOME
                    }
                    HomeScreen(
                        onAppClick = { packageName ->
                            navController.navigate(Screen.AppDetails.createRoute(packageName))
                        },
                        onProfileClick = {
                            navController.navigate(Screen.Profile.route)
                        }
                    )
                }
                composable(Screen.Plan.route) {
                    PlanScreen(
                        onNavigateToAddApps = { navController.navigate(Screen.AddApps.route) },
                        onAppClick = { packageName ->
                            navController.navigate(Screen.AppDetails.createRoute(packageName))
                        }
                    )
                }
                composable(Screen.AddApps.route) {
                    AddAppsScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(Screen.Notifications.route) {
                    NotificationsScreen(
                        onNotificationClick = { notification ->
                            navController.navigate(Screen.NotificationDetails.createRoute(notification.id))
                        }
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onThemeClick = { navController.navigate("settings_theme") },
                        onAccentClick = { navController.navigate("settings_accent") },
                        onProfileClick = { navController.navigate(Screen.Profile.route) },
                        onFaqClick = { /* TODO */ },
                        onFeedbackClick = { /* TODO */ },
                        onPrivacyClick = { /* TODO */ },
                        onVersionClick = { /* TODO */ }
                    )
                }
                composable("settings_theme") {
                    ThemeSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable("settings_accent") {
                    AccentSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.AppDetails.route,
                    arguments = listOf(navArgument("packageName") { type = NavType.StringType })
                ) {
                    AppDetailsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.NotificationDetails.route,
                    arguments = listOf(navArgument("id") { type = NavType.LongType })
                ) {
                    NotificationDetailScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
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
