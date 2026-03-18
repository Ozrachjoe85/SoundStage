package com.soundstage.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NeonGreen = Color(0xFF00FF88)
val DeepCharcoal = Color(0xFF0A0A0B)
val WarningRed = Color(0xFFFF3131)

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    background = DeepCharcoal,
    surface = Color(0xFF151516),
    onBackground = NeonGreen,
    onSurface = NeonGreen
)

@Composable
fun SoundStageTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColorScheme, content = content)
}
