package com.example.enaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.enaf.ui.screens.home.HomeScreen
import com.example.enaf.ui.screens.planner.PlannerRoute
import com.example.enaf.ui.screens.insights.InsightsScreen
import com.example.enaf.ui.screens.settings.SettingsScreen
import com.example.enaf.ui.screens.shop.ShopRoute
import com.example.enaf.ui.screens.roadmap.RoadmapRoute
import com.example.enaf.ui.screens.levels.LevelsRoute
import com.example.enaf.ui.screens.leaderboard.LeaderboardScreen
import com.example.enaf.ui.screens.achievements.AchievementsScreen
import com.example.enaf.ui.theme.EnafDarkBg
import com.example.enaf.ui.theme.EnafHeaderBg
import com.example.enaf.ui.theme.EnafTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnafTheme {
                EnafApp()
            }
        }
    }
}

@Composable
fun EnafApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo()),
        containerColor = EnafDarkBg,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = EnafHeaderBg,
            navigationBarContentColor = Color.White,
            navigationRailContainerColor = EnafHeaderBg
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen()
                AppDestinations.PLANNER -> PlannerRoute()
                AppDestinations.INSIGHTS -> InsightsScreen()
                AppDestinations.SHOP -> ShopRoute()
                AppDestinations.ROADMAP -> RoadmapRoute()
                AppDestinations.LEVELS -> LevelsRoute()
                AppDestinations.LEADERBOARD -> LeaderboardScreen()
                AppDestinations.ACHIEVEMENTS -> AchievementsScreen()
                AppDestinations.SETTINGS -> SettingsScreen()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    PLANNER("Planner", Icons.Default.DateRange),
    INSIGHTS("Insights", Icons.Default.Info),
    SHOP("Shop", Icons.Default.Storefront),
    ROADMAP("Roadmap", Icons.Default.Flag),
    LEVELS("Levels", Icons.Default.MilitaryTech),
    LEADERBOARD("Leaders", Icons.Default.Leaderboard),
    ACHIEVEMENTS("Badges", Icons.Default.MilitaryTech),
    SETTINGS("Settings", Icons.Default.Settings),
}
