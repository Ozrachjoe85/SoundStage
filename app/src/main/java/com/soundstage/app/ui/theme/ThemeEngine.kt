enum class UITheme(val primary: Color, val background: Color, val accent: Color) {
    PHOSPHOR(Color(0xFF00FF88), Color(0xFF0A0A0B), Color(0xFF004422)),
    AMBER(Color(0xFFFFB000), Color(0xFF100B00), Color(0xFF442E00)),
    CYBER(Color(0xFF00F0FF), Color(0xFF0D0015), Color(0xFF7000FF))
}

object ThemeState {
    var currentTheme by mutableStateOf(UITheme.PHOSPHOR)
}
