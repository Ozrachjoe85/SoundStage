package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.ui.theme.TacticalTheme
import com.soundstage.app.ui.theme.TacticalThemeManager

@Composable
fun SettingsScreen() {
    val currentTheme = TacticalThemeManager.currentTheme
    
    val rackBg = Color(0xFF0D0F12)
    val panelBg = Color(0xFF1A1D23)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(rackBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            ConfigHeader()
            
            // Theme Selector
            ThemeSelectorPanel(
                currentTheme = currentTheme,
                onThemeChange = { TacticalThemeManager.currentTheme = it }
            )
            
            // Audio Settings
            AudioSettingsPanel()
            
            // System Info
            SystemInfoPanel()
        }
    }
}

@Composable
fun ConfigHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp)
    ) {
        Text(
            text = "SYSTEM CONFIGURATION",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "SOUNDSTAGE AUDIO PROCESSOR v1.0",
            color = Color(0xFF00FF88),
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ThemeSelectorPanel(
    currentTheme: TacticalTheme,
    onThemeChange: (TacticalTheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "THEME SELECTION",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TacticalTheme.entries.forEach { theme ->
                ThemeOption(
                    theme = theme,
                    isSelected = theme == currentTheme,
                    onSelect = { onThemeChange(theme) }
                )
            }
        }
    }
}

@Composable
fun ThemeOption(
    theme: TacticalTheme,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                if (isSelected) theme.primary.copy(alpha = 0.1f) else Color(0xFF0D0F12),
                RoundedCornerShape(4.dp)
            )
            .border(
                2.dp,
                if (isSelected) theme.primary else Color(0xFF2A2F36),
                RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Color indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(theme.primary, RoundedCornerShape(2.dp))
            )
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(theme.hazard, RoundedCornerShape(2.dp))
            )
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = theme.displayName,
                color = if (isSelected) theme.primary else Color(0xFF9AA4AD),
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = theme.name.replace('_', ' '),
                color = Color(0xFF6B7280),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(theme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = theme.background,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AudioSettingsPanel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "AUDIO ENGINE CONFIGURATION",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingRow(
                label = "SAMPLE RATE",
                value = "48.0 kHz",
                color = Color(0xFF00FF88)
            )
            
            SettingRow(
                label = "BIT DEPTH",
                value = "24-bit",
                color = Color(0xFF00FF88)
            )
            
            SettingRow(
                label = "BUFFER SIZE",
                value = "512 samples",
                color = Color(0xFFFFB000)
            )
            
            SettingRow(
                label = "LATENCY",
                value = "~12 ms",
                color = Color(0xFFFFB000)
            )
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFF9AA4AD),
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(28.dp)
                .background(Color(0xFF000000), RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = value,
                color = color,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SystemInfoPanel() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "SYSTEM INFORMATION",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow(label = "VERSION", value = "1.0.0-ALPHA")
            InfoRow(label = "BUILD", value = "2026.03.18")
            InfoRow(label = "ENGINE", value = "MEDIA3-EXOPLAYER")
            InfoRow(label = "STATUS", value = "OPERATIONAL")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "© 2026 SOUNDSTAGE\nPROFESSIONAL AUDIO PROCESSING SYSTEM\nALL RIGHTS RESERVED",
                color = Color(0xFF5A6470),
                fontSize = 9.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color(0xFF9AA4AD),
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        
        Text(
            text = value,
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}
