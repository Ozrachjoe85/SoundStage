package com.soundstage.app.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class TacticalTheme(
    val displayName: String,
    val primary: Color,    // Phosphor glow
    val background: Color, // The "Void"
    val machine: Color,    // Surface metal
    val hazard: Color      // Warning/Redline
) {
    CADIA_STANDS("SECTION_01_GREEN", Color(0xFF00FF88), Color(0xFF050805), Color(0xFF1A221A), Color(0xFFFF0033)),
    MARS_ARCHIVE("SECTION_02_AMBER", Color(0xFFFFB000), Color(0xFF0D0800), Color(0xFF221500), Color(0xFFFF4400)),
    BLACK_ICE("SECTION_03_NEON", Color(0xFF00F0FF), Color(0xFF00050A), Color(0xFF0A1520), Color(0xFFBC00FF))
}

object TacticalThemeManager {
    var currentTheme by mutableStateOf(TacticalTheme.CADIA_STANDS)
}
