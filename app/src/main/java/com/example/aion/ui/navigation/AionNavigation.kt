package com.example.aion.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Plan : Screen("plan")
    object AddApps : Screen("add_apps")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
    
    // Details
    object AppDetails : Screen("app_details/{packageName}") {
        fun createRoute(packageName: String) = "app_details/$packageName"
    }
    object NotificationDetails : Screen("notification_details/{id}") {
        fun createRoute(id: Long) = "notification_details/$id"
    }
}
