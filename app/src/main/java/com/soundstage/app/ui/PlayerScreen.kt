package com.soundstage.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundstage.app.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    // Collect the current track state from your ViewModel here later
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Select a track to begin streaming",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
