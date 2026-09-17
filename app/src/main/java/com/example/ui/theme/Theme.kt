package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = AmberAccent,
    onPrimary = NavyDark,
    primaryContainer = AmberDark,
    onPrimaryContainer = Color.White,
    secondary = EmeraldTrust,
    onSecondary = NavyDark,
    background = DarkBackground,
    surface = DarkCard,
    surfaceVariant = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color.White,
    secondary = AmberAccent,
    onSecondary = NavyDark,
    secondaryContainer = AmberLight,
    onSecondaryContainer = AmberDark,
    tertiary = EmeraldTrust,
    onTertiary = Color.White,
    background = SlateBackground,
    surface = CardSurface,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderSubtle
)

@Composable
fun KaamWalaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded palette consistent
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
