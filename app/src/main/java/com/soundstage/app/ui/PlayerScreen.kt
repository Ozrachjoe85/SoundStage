package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val isPlaying by viewModel.isPlaying.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Phosphor Visualizer Placeholder
        Box(
            modifier = Modifier.fillMaxWidth().height(150.dp).background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center
        ) {
            Text("PHOSPHOR_ACTIVE", color = Color(0xFF00FF88))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Play/Pause Control
        Button(
            onClick = { viewModel.togglePlayback() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Text(if (isPlaying) "PAUSE" else "PLAY", color = Color(0xFF00FF88))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // The Dials that were causing the error
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AnalogDial(label = "GAIN")
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
                .background(Color(0xFF252525), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(2.dp, 20.dp).background(Color(0xFF00FF88)).align(Alignment.TopCenter))
        }
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
    }
}
