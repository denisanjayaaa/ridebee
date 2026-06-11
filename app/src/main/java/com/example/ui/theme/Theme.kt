package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MidnightColorScheme = darkColorScheme(
    primary = RacingOrange,
    primaryContainer = OrangeContainer,
    secondary = AmberYellow,
    background = MidnightBackground,
    surface = MidnightSurface,
    onPrimary = TextWhite,
    onSecondary = MidnightBackground,
    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextGray,
    outline = BorderColor,
    surfaceVariant = MidnightSurfaceDim
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark theme for the pure Midnight Amber look
    dynamicColor: Boolean = false, // Keep colors unified for brand styling
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MidnightColorScheme,
        typography = Typography,
        content = content
    )
}
