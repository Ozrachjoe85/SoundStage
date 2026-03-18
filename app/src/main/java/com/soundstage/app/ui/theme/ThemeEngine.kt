package com.soundstage.app.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class UITheme(
    val displayName: String,
    val primary: Color,
    val background: Color,
    val surface: Color,
    val accent: Color
) {
    PHOSPHOR("1982_GREEN", Color(0xFF00FF88), Color(0xFF0A0A0B), Color(0xFF151516), Color(0xFF004422)),
    AMBER("1979_TERMINAL", Color(0xFFFFB000), Color(0xFF100B00), Color(0xFF1A1400), Color(0xFF442E00)),
    CYBER("2026_NEON", Color(0xFF00F0FF), Color(0xFF0D0015), Color(0xFF1A0025), Color(0xFF7000FF))
}

object SoundStageThemeManager {
    var currentTheme by mutableStateOf(UITheme.PHOSPHOR)
}
