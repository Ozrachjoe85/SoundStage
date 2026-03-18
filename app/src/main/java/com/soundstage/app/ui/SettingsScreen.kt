package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.ui.theme.TacticalThemeManager
import com.soundstage.app.ui.theme.TacticalTheme

@Composable
fun SettingsScreen() {
    val theme = TacticalThemeManager.currentTheme

    Column(Modifier.fillMaxSize().background(theme.background).padding(20.dp)) {
        Text("COGITATOR_CONFIG // SYSTEM_PREFS", color = theme.primary, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(30.dp))
        
        Text("SELECT_INTERFACE_PALETTE", color = theme.primary.copy(alpha = 0.5f), fontSize = 10.sp)
        Spacer(Modifier.height(16.dp))

        TacticalTheme.entries.forEach { uiTheme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, if (theme == uiTheme) theme.primary else theme.primary.copy(alpha = 0.2f))
                    .clickable { TacticalThemeManager.currentTheme = uiTheme }
                    .padding(16.dp)
            ) {
                Text(
                    text = uiTheme.displayName,
                    color = if (theme == uiTheme) theme.primary else theme.primary.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                if (theme == uiTheme) {
                    Spacer(Modifier.weight(1f))
                    Text("[ACTIVE]", color = theme.primary, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }
            }
        }
    }
}
