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
fun PlayerScreen(viewModel: PlayerViewModel) {
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0B)).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // CRT Visualizer Monitor
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).background(Color(0xFF050505))
        ) {
            SegmentedVisualizer(isPlaying)
            // CRT Scanline Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (i in 0 until size.height.toInt() step 8) {
                    drawLine(Color.Black.copy(alpha = 0.3f), Offset(0f, i.toFloat()), Offset(size.width, i.toFloat()))
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Segmented Track Readout
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(currentSong?.title?.uppercase() ?: "NO_DATA", color = Color(0xFF00FF88), fontSize = 24.sp, fontFamily = FontFamily.Monospace)
            Text(currentSong?.artist?.uppercase() ?: "IDLE", color = Color(0xFF004422), fontSize = 14.sp, fontFamily = FontFamily.Monospace)
        }

        Spacer(modifier = Modifier.weight(1f))

        // The Transport Logic
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnalogDial("GAIN")
            Spacer(modifier = Modifier.width(40.dp))
            IconButton(
                onClick = { viewModel.togglePlayback() },
                modifier = Modifier.size(70.dp).background(Color(0xFF151516), CircleShape)
            ) {
                Text(if (isPlaying) "||" else ">>", color = Color(0xFF00FF88), fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(40.dp))
            AnalogDial("VOL")
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
            // Scale the magnitude to fit the screen height
            val scaledHeight = (magnitude * 2f).coerceIn(10f, size.height)
            
            drawRect(
                color = if (scaledHeight > size.height * 0.8f) Color(0xFFFF3131) else Color(0xFF00FF88),
                topLeft = Offset(i * spacing, size.height - scaledHeight),
                size = androidx.compose.ui.geometry.Size(spacing - 4f, scaledHeight)
            )
        }
    }
}

@Composable
fun AnalogDial(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(50.dp).background(Color(0xFF151516), CircleShape)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(Color(0xFF00FF88), center, Offset(size.width/2, 5f), 4f)
            }
        }
        Text(label, color = Color(0xFF004422), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
}
