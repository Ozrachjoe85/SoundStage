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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.soundstage.app.ui.components.AnalogDial
import com.soundstage.app.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onOpenLibrary: () -> Unit
) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Utility Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SOUNDSTAGE // V1.0.4",
                color = Color(0xFF004422),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            TextButton(onClick = onOpenLibrary) {
                Text(
                    text = "[SCAN_DATABASE]",
                    color = Color(0xFF00FF88),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Phosphor CRT Visualizer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF050505)),
            contentAlignment = Alignment.Center
        ) {
            SegmentedVisualizer(viewModel)
            
            // Scanline Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scanlineSpacing = 8f
                for (i in 0 until size.height.toInt() step scanlineSpacing.toInt()) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.4f),
                        start = Offset(0f, i.toFloat()),
                        end = Offset(size.width, i.toFloat()),
                        strokeWidth = 2f
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Metadata Readout
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = currentSong?.title?.uppercase() ?: "SIGNAL_LOST",
                color = Color(0xFF00FF88),
                fontSize = 26.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
            Text(
                text = currentSong?.artist?.uppercase() ?: "SOURCE: UNKNOWN",
                color = Color(0xFF004422),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 4. Transport & Analog Deck
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnalogDial(label = "GAIN") { /* Logic handled in VM */ }

            IconButton(
                onClick = { viewModel.togglePlayback() },
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF151516), CircleShape)
            ) {
                Text(
                    text = if (isPlaying) "||" else ">>",
                    color = Color(0xFF00FF88),
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            AnalogDial(label = "MASTER") { /* Logic handled in VM */ }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SegmentedVisualizer(viewModel: PlayerViewModel) {
    val magnitudes by viewModel.visualizerData.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val bars = 24
        val spacing = size.width / bars
        
        magnitudes.forEachIndexed { i, magnitude ->
            // Scale magnitude for visual impact
            val scaledHeight = (magnitude * 2.5f).coerceIn(8f, size.height)
            
            // Draw the segmented phosphor bar
            drawRect(
                color = if (scaledHeight > size.height * 0.85f) Color(0xFFFF3131) else Color(0xFF00FF88),
                topLeft = Offset(i * spacing, size.height - scaledHeight),
                size = androidx.compose.ui.geometry.Size(spacing - 6f, scaledHeight)
            )
        }
    }
}
