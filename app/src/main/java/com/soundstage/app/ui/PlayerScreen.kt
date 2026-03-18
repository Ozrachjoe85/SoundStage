package com.soundstage.app.ui

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
import com.soundstage.app.viewmodel.PlayerViewModel

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
            .background(Color(0xFF121212)) // Charcoal Background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SOUNDSTAGE // V1.0",
                color = Color(0xFF00FF88).copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Button(
                onClick = onOpenLibrary,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Text("BROWSE", color = Color(0xFF00FF88), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 2. Phosphor Visualizer Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF0A0A0A)),
            contentAlignment = Alignment.Center
        ) {
            PhosphorVisualizer(isPlaying = isPlaying)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. Track Metadata
        Text(
            text = currentSong?.title?.uppercase() ?: "NO MEDIA LOADED",
            color = Color(0xFF00FF88),
            fontSize = 22.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = currentSong?.artist?.uppercase() ?: "SYSTEM IDLE",
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.weight(1f))

        // 4. Transport Controls
        IconButton(
            onClick = { viewModel.togglePlayback() },
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF1A1A1A), CircleShape)
        ) {
            // Simple Play/Pause ASCII-style icons
            Text(
                text = if (isPlaying) "||" else "▶",
                color = Color(0xFF00FF88),
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // 5. Analog Control Deck
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnalogDial(label = "INPUT GAIN")
            AnalogDial(label = "MASTER VOL")
        }
        
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PhosphorVisualizer(isPlaying: Boolean) {
    // This draws a static grid/line. In the next step, we hook this to FFT data.
    Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        val width = size.width
        val height = size.height
        val barCount = 20
        val barSpacing = width / barCount

        for (i in 0 until barCount) {
            val x = i * barSpacing
            // Random-ish heights for now to simulate activity
            val barHeight = if (isPlaying) (20..height.toInt()).random().toFloat() else 5f
            drawLine(
                color = Color(0xFF00FF88),
                start = Offset(x, height),
                end = Offset(x, height - barHeight),
                strokeWidth = 8f
            )
        }
    }
}

@Composable
fun AnalogDial(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color(0xFF222222), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Dial Indicator
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                drawLine(
                    color = Color(0xFF00FF88),
                    start = center,
                    end = Offset(size.width / 2, 15f),
                    strokeWidth = 4f
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
