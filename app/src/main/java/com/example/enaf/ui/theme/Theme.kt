package com.example.enaf.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EnafActionBlue,
    secondary = EnafPink,
    tertiary = EnafSuccess,
    background = EnafDarkBg,
    surface = EnafCardBg,
    onPrimary = EnafTextPrimary,
    onSecondary = EnafTextPrimary,
    onTertiary = EnafTextPrimary,
    onBackground = EnafTextPrimary,
    onSurface = EnafTextPrimary,
    error = EnafErrorRed,
    onError = EnafTextPrimary,
    outline = EnafOutline,
    outlineVariant = EnafBorder
)

private val LightColorScheme = lightColorScheme(
    primary = EnafActionBlue,
    secondary = EnafPink,
    tertiary = EnafSuccess,
    background = Color(0xFFF8FAFC), // Very light blue-grey
    surface = Color(0xFFFFFFFF), // White
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A), // Dark slate for contrast
    onSurface = Color(0xFF0F172A),
    error = EnafErrorRed,
    onError = Color.White,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun EnafTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
