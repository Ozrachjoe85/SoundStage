package com.soundstage.app.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.soundstage.app.ui.components.AnalogDial
import com.soundstage.app.ui.theme.SoundStageThemeManager
import com.soundstage.app.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onOpenLibrary: () -> Unit,
    onOpenQueue: () -> Unit = {} // Added for the new Queue route
) {
    val theme = SoundStageThemeManager.currentTheme
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER BAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SYS_STATUS: OPTIMAL",
                color = theme.accent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Row {
                TextButton(onClick = onOpenQueue) {
                    Text("[QUEUE]", color = theme.primary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                TextButton(onClick = onOpenLibrary) {
                    Text("[DATA]", color = theme.primary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- THE CRT MONITOR (Visualizer) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            SegmentedVisualizer(viewModel)
            
            // Scanline Overlay (Always Black/Transparent for depth)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scanlineSpacing = 6f
                for (i in 0 until size.height.toInt() step scanlineSpacing.toInt()) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.5f),
                        start = Offset(0f, i.toFloat()),
                        end = Offset(size.width, i.toFloat()),
                        strokeWidth = 1.5f
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- TRACK METADATA ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = currentSong?.title?.uppercase() ?: "NO_SIGNAL",
                color = theme.primary,
                fontSize = 26.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )
            Text(
                text = currentSong?.artist?.uppercase() ?: "SOURCE_UNKNOWN",
                color = theme.accent,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- ANALOG DECK CONTROLS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnalogDial(label = "GAIN") {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }

            // Transport Center
            IconButton(
                onClick = { 
                    viewModel.togglePlayback()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier
                    .size(85.dp)
                    .background(theme.surface, CircleShape)
            ) {
                Text(
                    text = if (isPlaying) "||" else ">>",
                    color = theme.primary,
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            AnalogDial(label = "MASTER") {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SegmentedVisualizer(viewModel: PlayerViewModel) {
    val theme = SoundStageThemeManager.currentTheme
    val magnitudes by viewModel.visualizerData.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val bars = 24
        val spacing = size.width / bars
        
        magnitudes.forEachIndexed { i, magnitude ->
            // Dynamic height scaling
            val scaledHeight = (magnitude * 3f).coerceIn(10f, size.height)
            
            // Peak indicator logic (Redline)
            val barColor = if (scaledHeight > size.height * 0.85f) {
                Color(0xFFFF3131) // Redline warning
            } else {
                theme.primary
            }

            drawRect(
                color = barColor,
                topLeft = Offset(i * spacing, size.height - scaledHeight),
                size = androidx.compose.ui.geometry.Size(spacing - 4f, scaledHeight)
            )
        }
    }
}
