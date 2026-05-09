package com.example.application.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    val primary = Color(0xD4A5F7)
    val primaryLight = Color(0xE8D5FF)
    val primaryDark = Color(0xB896E8)
    
    val secondary = Color(0xE6CCFF)
    val secondaryLight = Color(0xF5E5FF)
    val secondaryDark = Color(0xD9B5FF)
    
    val tertiary = Color(0xF0D9FF)
    val tertiaryLight = Color(0xF8ECFF)
    val tertiaryDark = Color(0xE6CCFF)
    
    val background = Color(0xFAF7FF)
    val surface = Color(0xFFFFFF)
    val surfaceVariant = Color(0xF0E6FF)
    
    val onPrimary = Color(0x4A1D7F)
    val onSecondary = Color(0x5A3A75)
    val onBackground = Color(0x2D1B4E)
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
        
        outline = Color(0xD4A5F7),
        outlineVariant = Color(0xE6CCFF)
    )
}
