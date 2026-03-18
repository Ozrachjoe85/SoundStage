package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soundstage.app.data.Song
import com.soundstage.app.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel, onSongClick: (Song) -> Unit) {
    val songs by viewModel.songs.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadLibrary() }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        Text(
            "SYSTEM_LIBRARY", 
            color = Color(0xFF00FF88), 
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(songs) { song ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSongClick(song) }
                        .padding(16.dp)
                ) {
                    Text(text = song.title, color = Color(0xFF00FF88))
                    Text(text = song.artist, color = Color.Gray)
                }
            }
        }
    }
}
