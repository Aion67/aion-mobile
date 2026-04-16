package com.example.enaf.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EnafActionBlue,
    secondary = EnafCardBg,
    background = EnafDarkBg,
    surface = EnafCardBg,
    onPrimary = Color.White,
    onBackground = EnafTextPrimary,
    onSurface = EnafTextPrimary,
    error = EnafErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = EnafActionBlue,
    secondary = Color.White,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = EnafErrorRed
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
