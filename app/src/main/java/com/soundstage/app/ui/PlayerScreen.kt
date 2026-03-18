package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerScreen(viewModel: AudioViewModel) {
    val songs by viewModel.uiState.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()

    // Load music when the screen first opens
    LaunchedEffect(Unit) {
        viewModel.loadMusic()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Track Info Display
        Spacer(modifier = Modifier.height(60.dp))
        
        Text(
            text = currentSong?.title ?: "SCANNING DISK...",
            color = Color(0xFF00FF88),
            fontSize = 28.sp
        )
        Text(
            text = currentSong?.artist ?: "READY FOR INPUT",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Play Button for the first found song (as a test)
        if (songs.isNotEmpty() && currentSong == null) {
            Button(
                onClick = { viewModel.playSong(songs[0]) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Text("INITIALIZE PLAYBACK", color = Color(0xFF00FF88))
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))

        // Analog Dials
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AnalogDial(label = "GAIN")
            AnalogDial(label = "VOL")
        }
    }
}
