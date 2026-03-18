package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerScreen(viewModel: Any) { // ViewModel logic will be injected here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Header Area
        Text(
            text = "SOUNDSTAGE // ANALOG-DIGITAL",
            color = Color(0xFF00FF88).copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )

        // 2. The Visualizer (Placeholder for the FFT Bars)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center
        ) {
            Text("VISUALIZER_ACTIVE", color = Color(0xFF00FF88), fontFamily = FontFamily.Monospace)
        }

        // 3. Track Info
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("NO TRACK LOADED", color = Color(0xFF00FF88), fontSize = 24.sp)
            Text("WAITING FOR MEDIA...", color = Color.Gray, fontSize = 14.sp)
        }

        // 4. Analog Controls (Knobs/Dials)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnalogDial(label = "GAIN")
            AnalogDial(label = "EQ")
            AnalogDial(label = "VOL")
        }
    }
}

@Composable
fun AnalogDial(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFF252525), shape = androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Stylized Indicator line
            Box(modifier = Modifier.size(2.dp, 20.dp).background(Color(0xFF00FF88)).align(Alignment.TopCenter))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
    }
}
