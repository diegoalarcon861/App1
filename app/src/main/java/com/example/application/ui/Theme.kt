package com.example.application.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    val primary = Color(0xFFE76F51)
    val primaryLight = Color(0xFFF6B8A9)
    val primaryDark = Color(0xFFC94A2A)

    val secondary = Color(0xFFE9C46A)
    val secondaryLight = Color(0xFFF3DC9C)
    val secondaryDark = Color(0xFFB7902F)

    val tertiary = Color(0xFF264653)
    val tertiaryLight = Color(0xFF416877)
    val tertiaryDark = Color(0xFF162B33)

    val background = Color(0xFFFFF8F3)
    val surface = Color(0xFFFFFFFF)
    val surfaceVariant = Color(0xFFF5E6D8)

    val onPrimary = Color(0xFFFFFFFF)
    val onSecondary = Color(0xFF2F2508)
    val onBackground = Color(0xFF2C211C)
}

fun createAppColorScheme(): ColorScheme {
    return lightColorScheme(
        primary = AppColors.primary,
        onPrimary = AppColors.onPrimary,
        primaryContainer = AppColors.primaryLight,
        onPrimaryContainer = AppColors.onPrimary,
        
        secondary = AppColors.secondary,
        onSecondary = AppColors.onSecondary,
        secondaryContainer = AppColors.secondaryLight,
        onSecondaryContainer = AppColors.onSecondary,
        
        tertiary = AppColors.tertiary,
        onTertiary = AppColors.onPrimary,
        tertiaryContainer = AppColors.tertiaryLight,
        onTertiaryContainer = AppColors.onPrimary,
        
        background = AppColors.background,
        onBackground = AppColors.onBackground,
        
        surface = AppColors.surface,
        onSurface = AppColors.onBackground,
        surfaceVariant = AppColors.surfaceVariant,
        onSurfaceVariant = AppColors.onSecondary,
        
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),
        
        outline = Color(0xFFCAAB8F),
        outlineVariant = Color(0xFFE3CEBB)
    )
}
