package com.soundstage.app.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.soundstage.app.data.MusicRepository

@Composable
fun LibraryScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { MusicRepository(context.contentResolver) }
    val tracks = remember { repo.getAllTracks() }

    LazyColumn {
        items(tracks) { track ->
            ListItem(
                headlineContent = { Text(track.title) },
                supportingContent = { Text(track.artist) },
                modifier = androidx.compose.ui.Modifier.clickable {
                    navController.navigate("player")
                }
            )
        }
    }
}
