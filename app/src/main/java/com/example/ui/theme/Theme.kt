package com.example.ui.theme

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
    primary = RekiyaPrimaryDark,
    onPrimary = RekiyaOnPrimaryDark,
    primaryContainer = RekiyaPrimaryContainerDark,
    onPrimaryContainer = RekiyaOnPrimaryContainerDark,
    secondary = RekiyaSecondaryDark,
    onSecondary = RekiyaOnSecondaryDark,
    secondaryContainer = RekiyaSecondaryContainerDark,
    tertiary = RekiyaTertiaryDark,
    background = RekiyaBackgroundDark,
    surface = RekiyaSurfaceDark,
    onSurface = RekiyaOnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = RekiyaPrimary,
    onPrimary = RekiyaOnPrimary,
    primaryContainer = RekiyaPrimaryContainer,
    onPrimaryContainer = RekiyaOnPrimaryContainer,
    secondary = RekiyaSecondary,
    onSecondary = RekiyaOnSecondary,
    secondaryContainer = RekiyaSecondaryContainer,
    onSecondaryContainer = RekiyaOnSecondaryContainer,
    tertiary = RekiyaTertiary,
    onTertiary = RekiyaOnTertiary,
    tertiaryContainer = RekiyaTertiaryContainer,
    onTertiaryContainer = RekiyaOnTertiaryContainer,
    background = RekiyaBackground,
    onBackground = RekiyaOnBackground,
    surface = RekiyaSurface,
    onSurface = RekiyaOnSurface,
    surfaceVariant = RekiyaSurfaceVariant,
    onSurfaceVariant = RekiyaOnSurfaceVariant,
    outline = RekiyaOutline
)

@Composable
fun RekiyaSoyamuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
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
