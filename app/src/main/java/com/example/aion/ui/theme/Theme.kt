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
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBrand,
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

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrand,
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

@Composable
fun AionTheme(
    themeMode: String = "System",
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "Light" -> false
        "Dark" -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}