package com.example.aion.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

fun getAccentColorFromLabel(label: String): Color = when (label) {
    "Purple" -> Color(0xFF6750A4)
    "Pink" -> Color(0xFFEC4899)
    "Green" -> Color(0xFF10B981)
    "Blue" -> Color(0xFF007BFF)
    "Orange" -> Color(0xFFFF9800)
    "Teal" -> Color(0xFF14B8A6)
    "Red" -> Color(0xFFF44336)
    "Gray" -> Color(0xFF79747E)
    else -> Color(0xFF6750A4) // Default to purple
}

private fun createLightColorScheme(primaryColor: Color) = lightColorScheme(
    primary = primaryColor,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surface = SchemesSurface,
    onSurface = SchemesOnSurface,
    surfaceVariant = SchemesSurfaceVariant,
    onSurfaceVariant = SchemesOnSurfaceVariant,
    outline = SchemesOutline,
    background = SchemesSurface,
    onBackground = SchemesOnSurface
)

private fun createDarkColorScheme(primaryColor: Color) = darkColorScheme(
    primary = primaryColor,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    background = DarkSurface,
    onBackground = DarkOnSurface
)

@Composable
fun AionTheme(
    themeMode: String = "System",
    accentColor: String = "Purple",
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "Light" -> false
        "Dark" -> true
        else -> isSystemInDarkTheme()
    }

    val primaryColor = getAccentColorFromLabel(accentColor)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> createDarkColorScheme(primaryColor)
        else -> createLightColorScheme(primaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}