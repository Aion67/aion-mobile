package com.example.enaf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

    // NavigationSuiteScaffold handles the transition between BottomBar and NavigationRail
    NavigationSuiteScaffold(
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
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Placeholder for NavHost - Logic for switching screens goes here
            when (currentDestination) {
                AppDestinations.HOME -> PlaceholderScreen("Home Dashboard", innerPadding)
                AppDestinations.PLANNER -> PlaceholderScreen("Screen Time Planner", innerPadding)
                AppDestinations.INSIGHTS -> PlaceholderScreen("Habit Insights", innerPadding)
                AppDestinations.SETTINGS -> PlaceholderScreen("Settings", innerPadding)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, padding: androidx.compose.foundation.layout.PaddingValues) {
    Text(
        text = title,
        modifier = Modifier.padding(padding).fillMaxSize()
    )
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    PLANNER("Planner", Icons.Default.DateRange),
    INSIGHTS("Insights", Icons.Default.Info),
    SETTINGS("Settings", Icons.Default.Settings),
}
