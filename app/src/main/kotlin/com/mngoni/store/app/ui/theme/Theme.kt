package com.mngoni.store.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BrandGreen = Color(0xFF1B5E3B)
private val BrandGreenLight = Color(0xFF2E7D57)
private val BrandOrange = Color(0xFFE8893A)
private val Canvas = Color(0xFFF7F5F0)

private val LightColors = lightColorScheme(
    primary = BrandGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDEDDC),
    onPrimaryContainer = Color(0xFF0A3B24),
    secondary = BrandOrange,
    onSecondary = Color.White,
    background = Canvas,
    onBackground = Color(0xFF1A1C1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1A),
    surfaceVariant = Color(0xFFE8EEE9),
    error = Color(0xFFB3261E),
)

private val DarkColors = darkColorScheme(
    primary = BrandGreenLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0A3B24),
    onPrimaryContainer = Color(0xFFCDEDDC),
    secondary = BrandOrange,
    onSecondary = Color(0xFF2A1608),
    background = Color(0xFF121613),
    onBackground = Color(0xFFE6ECE7),
    surface = Color(0xFF1A201C),
    onSurface = Color(0xFFE6ECE7),
    error = Color(0xFFFFB4AB),
)

@Composable
fun MngoniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
