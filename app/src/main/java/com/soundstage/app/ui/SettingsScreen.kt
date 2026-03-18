package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.ui.theme.SoundStageThemeManager
import com.soundstage.app.ui.theme.UITheme

@Composable
fun SettingsScreen() {
    val theme = SoundStageThemeManager.currentTheme

    Column(Modifier.fillMaxSize().background(theme.background).padding(20.dp)) {
        Text("SYSTEM_CONFIGURATION // SETTINGS", color = theme.primary, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(30.dp))
        
        Text("INTERFACE_SKIN_SELECTION", color = theme.accent, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))

        UITheme.values().forEach { uiTheme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, if (theme == uiTheme) theme.primary else theme.accent)
                    .clickable { SoundStageThemeManager.currentTheme = uiTheme }
                    .padding(16.dp)
            ) {
                Text(
                    text = uiTheme.displayName,
                    color = if (theme == uiTheme) theme.primary else theme.accent,
                    fontFamily = FontFamily.Monospace
                )
                if (theme == uiTheme) {
                    Spacer(Modifier.weight(1f))
                    Text("[ACTIVE]", color = theme.primary, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
