package com.soundstage.app.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.soundstage.app.ui.components.AnalogDial
import com.soundstage.app.ui.theme.SoundStageThemeManager
import com.soundstage.app.viewmodel.PlayerViewModel

@OptIn(UnsafeOptInUsageError::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel, onOpenLibrary: () -> Unit) {
    val theme = SoundStageThemeManager.currentTheme
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(theme.background)) {
        // --- 1. TACTICAL HUD HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("MODE: HI-FI_STEREO", color = theme.accent, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                Text("DECODER: FLAC_LE", color = theme.primary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            IconButton(onClick = onOpenLibrary, modifier = Modifier.border(1.dp, theme.primary, RoundedCornerShape(4.dp))) {
                Text("DB", color = theme.primary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }
        }

        // --- 2. MIRRORED FREQUENCY LANDSCAPE ---
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).background(Color.Black.copy(alpha = 0.3f))) {
            HifiWaveform(viewModel)
        }

        // --- 3. METADATA & PROGRESS ---
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                text = currentSong?.title?.uppercase() ?: "SIGNAL_IDLE",
                style = TextStyle(
                    color = theme.primary,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Monospace,
                    shadow = Shadow(color = theme.primary.copy(alpha = 0.5f), blurRadius = 8f)
                )
            )
            Text(currentSong?.artist?.uppercase() ?: "NO_SOURCE", color = theme.accent, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Progress Bar (Linear Retro)
            LinearProgressIndicator(
                progress = 0.4f, // Mock progress
                modifier = Modifier.fillMaxWidth().height(2.dp),
                color = theme.primary,
                trackColor = theme.accent.copy(alpha = 0.2f)
            )
        }

        // --- 4. DATA WIDGETS (Musicolet Style) ---
        Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoWidget("BPM", "128")
            InfoWidget("KEY", "Am")
            InfoWidget("SR", "44.1kHz")
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- 5. THE CONTROL DECK ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            color = theme.surface,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnalogDial("GAIN")
                IconButton(
                    onClick = { viewModel.togglePlayback() },
                    modifier = Modifier.size(70.dp).background(theme.background, CircleShape).border(2.dp, theme.primary, CircleShape)
                ) {
                    Text(if (isPlaying) "||" else ">>", color = theme.primary, fontSize = 24.sp)
                }
                AnalogDial("VOL")
            }
        }
    }
}

@Composable
fun InfoWidget(label: String, value: String) {
    val theme = SoundStageThemeManager.currentTheme
    Column {
        Text(label, color = theme.accent, fontSize = 9.sp)
        Text(value, color = theme.primary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}
