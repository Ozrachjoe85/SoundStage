package com.soundstage.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text

@Composable // <--- Make sure this is here!
fun PlayerScreen(viewModel: PlayerViewModel) {
    Column {
        Text(text = "Now Playing")
        // ... rest of your code
    }
}
