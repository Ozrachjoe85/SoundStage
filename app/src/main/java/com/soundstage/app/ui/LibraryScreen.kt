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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.data.Song
import com.soundstage.app.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel, onSongClick: (Song) -> Unit) {
    val songs by viewModel.songs.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadLibrary() }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0B)).padding(16.dp)) {
        Text("DATABASE_QUERY // RESULTS", color = Color(0xFF004422), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(10.dp))
        
        LazyColumn {
            items(songs) { song ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSongClick(song) }.padding(vertical = 12.dp)
                ) {
                    Text("[+]", color = Color(0xFF00FF88), modifier = Modifier.padding(end = 8.dp))
                    Column {
                        Text(song.title.uppercase(), color = Color(0xFF00FF88), fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text(song.artist.uppercase(), color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
