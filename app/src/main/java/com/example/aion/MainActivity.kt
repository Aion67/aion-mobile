package com.example.aion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.aion.ui.navigation.GlassBottomBar
import com.example.aion.ui.screens.*
import com.example.aion.ui.theme.AionTheme
import com.example.aion.ui.viewmodels.SettingsViewModel
import com.example.aion.ui.viewmodels.ProfileViewModel
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
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val onboardingViewModel: com.example.aion.ui.viewmodels.OnboardingViewModel = hiltViewModel()
    val onboardingState by onboardingViewModel.uiState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()

    val startDestination = if (onboardingState.isCompleted) Screen.Home.route else Screen.Onboarding.route

    // Don't show navigation bar on onboarding
    val showNavBar = onboardingState.isCompleted

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                enterTransition = {
                    fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.92f, animationSpec = tween(500))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.92f, animationSpec = tween(400))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 1.08f, animationSpec = tween(500))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 1.08f, animationSpec = tween(400))
                }
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
                        },
                        avatarUri = profileState.profile.avatarUri
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onThemeClick = { navController.navigate("settings_theme") },
                        onAccentClick = { navController.navigate("settings_accent") },
                        onProfileClick = { navController.navigate(Screen.Profile.route) },
                        onOnlineSetupClick = { navController.navigate(Screen.OnlineModeSetup.route) },
                        onFaqClick = { navController.navigate(Screen.SettingsDetail.createRoute("FAQ")) },
                        onFeedbackClick = {
                            val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                                data = android.net.Uri.parse("mailto:support@aion.example.com")
                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Aion Feedback")
                            }
                            navController.context.startActivity(intent)
                        },
                        onPrivacyClick = { navController.navigate(Screen.SettingsDetail.createRoute("Privacy Policy")) },
                        onVersionClick = { navController.navigate(Screen.SettingsDetail.createRoute("Version")) }
                    )
                }
                composable("settings_theme") {
                    ThemeSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable("settings_accent") {
                    AccentSettingsScreen(onBack = { navController.popBackStack() })
                }
                composable(Screen.OnlineModeSetup.route) {
                    OnlineModeSetupScreen(
                        onBack = { navController.popBackStack() },
                        onComplete = {
                            settingsViewModel.updateAppMode("online")
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    route = Screen.SettingsDetail.route,
                    arguments = listOf(navArgument("title") { type = NavType.StringType })
                ) { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title") ?: ""
                    val body = when (title) {
                        "FAQ" -> "1. How does the score work?\nScore goes up when you stay under your limits. Overshooting reduces it.\n\n2. Why do I need permissions?\nAion needs Usage Access to track your time and Overlay permission to show you alerts when you exceed limits."
                        "Privacy Policy" -> "All your data stays locally on your device unless you use Online Mode, in which case game progress is synced to the cloud."
                        "Version" -> "Aion v1.0.0-alpha\nBuild: 2026"
                        else -> "Content not found."
                    }
                    SettingsDetailScreen(
                        title = title,
                        body = body,
                        onBack = { navController.popBackStack() }
                    )
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

            if (showNavBar) {
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    GlassBottomBar(
                        currentDestination = currentDestination,
                        onDestinationSelected = { destination ->
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
