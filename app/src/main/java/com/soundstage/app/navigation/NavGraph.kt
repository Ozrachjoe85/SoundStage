package com.soundstage.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.soundstage.app.ui.*
import com.soundstage.app.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val playerVm: PlayerViewModel = viewModel()
    val libraryVm: LibraryViewModel = viewModel()

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF0A0A0B)).padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DeckButton("DECK") { navController.navigate("player") }
                DeckButton("DATA") { navController.navigate("library") }
                DeckButton("CORE") { /* Settings placeholder */ }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = "player",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("player") { PlayerScreen(viewModel = playerVm) }
            composable("library") {
                LibraryScreen(viewModel = libraryVm) { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                }
            }
        }
    }
}

@Composable
fun DeckButton(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text("[$label]", color = Color(0xFF00FF88), fontSize = 12.sp)
    }
}
