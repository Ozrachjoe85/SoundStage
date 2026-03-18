package com.soundstage.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography

// Semantic Token Mapping for High-Grade Optimal Performance
private val DarkColorPalette = darkColorScheme(
    primary = PrimitiveRetroGreen,       // Phosphor Green (image_1.png)
    secondary = PrimitiveRetroCyan,
    background = PrimitiveCharcoalBlack, // Optimized background (image_1.png)
    surface = PrimitiveDeepGrey,
    onPrimary = PrimitiveCharcoalBlack, // High Contrast
    onBackground = PrimitiveCrispWhite, // Text on dark (image_1.png)
    onSurface = PrimitiveCrispWhite,   // Icons on dark
    onSurfaceVariant = PrimitiveRetroOrange // Accent on surface (EQ analog knobs)
)

private val LightColorPalette = lightColorScheme(
    primary = PrimitiveRetroBlue,        // Electric Blue (image_4.png)
    secondary = PrimitiveRetroCyan,
    background = PrimitiveSoftWhite,      // Optimized light background (image_4.png)
    surface = PrimitiveCrispWhite,
    onPrimary = PrimitiveCrispWhite,     // Text on Blue
    onBackground = PrimitiveMatteBlack, // Text on light (image_4.png)
    onSurface = PrimitiveMatteBlack,   // Icons on light (image_4.png)
    onSurfaceVariant = PrimitiveRetroCyan // Accent on light surface (Visualization)
)

@Composable
fun SoundStageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
    colorScheme = colors,
    // Explicitly use the Material3 Typography instead of the generic one
    typography = androidx.compose.material3.Typography(), 
    content = content
)
}
