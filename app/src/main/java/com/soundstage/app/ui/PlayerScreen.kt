package com.soundstage.app.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.soundstage.app.ui.components.AnalogDial
import com.soundstage.app.ui.theme.TacticalThemeManager
import com.soundstage.app.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel, onOpenLibrary: () -> Unit) {
    val theme = TacticalThemeManager.currentTheme
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val magnitudes by viewModel.visualizerData.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(theme.background).padding(16.dp)) {
        // --- SYSTEM HEADER ---
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("AUDIO_COGITATOR_V4.2", color = theme.primary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text("NEURAL_LINK: ACTIVE", color = theme.primary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }

        Spacer(Modifier.height(12.dp))

        // --- THE WAVEFORM PIT (Tactical Visualizer) ---
        Box(modifier = Modifier.fillMaxWidth().height(240.dp).border(1.dp, theme.primary.copy(0.2f))) {
            WaveformCogitator(magnitudes)
        }

        // --- TRACK MANIFEST ---
        Column(Modifier.padding(vertical = 20.dp)) {
            Text(
                text = "FILE: ${currentSong?.title?.uppercase() ?: "NO_SIGNAL"}",
                color = theme.primary,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "SOURCE: ${currentSong?.artist?.uppercase() ?: "UNKNOWN_SECTOR"}",
                color = theme.primary.copy(0.6f),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // --- AUDIOPHILE DATA GRID ---
        Row(Modifier.fillMaxWidth().weight(1f)) {
            Column(Modifier.weight(1f)) {
                TacticalDataPoint("BITRATE", "1411 KBPS")
                TacticalDataPoint("FORMAT", "FLAC_LOSSLESS")
                TacticalDataPoint("BUFFER", "2048MS")
            }
            
            // Mechanical Control Center
            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = { viewModel.togglePlayback() },
                    modifier = Modifier.size(80.dp).border(2.dp, theme.primary, RoundedCornerShape(8.dp))
                ) {
                    Text(if (isPlaying) "STOP" else "ENGAGE", color = theme.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- ANALOG SUB-DECK ---
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AnalogDial("DRIVE")
            AnalogDial("VOX")
            AnalogDial("MASTER")
        }
    }
}

@Composable
fun WaveformCogitator(magnitudes: FloatArray) {
    val theme = TacticalThemeManager.currentTheme
    Canvas(Modifier.fillMaxSize().padding(8.dp)) {
        val barWidth = size.width / (magnitudes.size)
        val centerY = size.height / 2

        magnitudes.forEachIndexed { i, mag ->
            val h = (mag * 2.8f).coerceIn(2f, centerY)
            val color = if (h > centerY * 0.85f) theme.hazard else theme.primary
            
            // Symmetrical Cogitator Stack
            drawRect(
                color = color,
                topLeft = Offset(i * barWidth, centerY - h),
                size = Size(barWidth - 4f, h * 2)
            )
        }
    }
}

@Composable
fun TacticalDataPoint(label: String, value: String) {
    val theme = TacticalThemeManager.currentTheme
    Column(Modifier.padding(vertical = 6.dp)) {
        Text(label, color = theme.primary.copy(0.4f), fontSize = 9.sp)
        Text(value, color = theme.primary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
    }
}
