package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CinemaAmber,
    onPrimary = ObsidianBlack,
    primaryContainer = CinemaGoldContainer,
    onPrimaryContainer = CinemaOnGoldContainer,
    secondary = GradingTeal,
    onSecondary = ObsidianBlack,
    secondaryContainer = GradingTealContainer,
    onSecondaryContainer = GradingOnTealContainer,
    tertiary = FilmCoral,
    onTertiary = Color.White,
    tertiaryContainer = FilmCoralContainer,
    onTertiaryContainer = FilmOnCoralContainer,
    background = ObsidianBlack,
    onBackground = StudioTextPrimaryDark,
    surface = StudioSurfaceDark,
    onSurface = StudioTextPrimaryDark,
    surfaceVariant = StudioCardDark,
    onSurfaceVariant = StudioTextSecondaryDark,
    outline = StudioCardBorder,
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = CinemaAmberDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = GradingTealDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF164E63),
    tertiary = FilmCoral,
    onTertiary = Color.White,
    background = StudioSurfaceLight,
    onBackground = StudioTextPrimaryLight,
    surface = StudioCardLight,
    onSurface = StudioTextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = StudioTextSecondaryLight,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun SaiGraphicsTheme(
    darkTheme: Boolean = true, // Default to sleek studio dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
