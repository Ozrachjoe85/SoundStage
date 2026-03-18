package com.soundstage.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.soundstage.app.ui.*
import com.soundstage.app.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val playerVm: PlayerViewModel = viewModel()
    val libraryVm: LibraryViewModel = viewModel()
    val eqVm: EQViewModel = viewModel()

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF0A0A0B)),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = { navController.navigate("player") }) {
                    Text("[DECK]", color = Color(0xFF00FF88))
                }
                TextButton(onClick = { navController.navigate("library") }) {
                    Text("[DATA]", color = Color(0xFF00FF88))
                }
                TextButton(onClick = { navController.navigate("eq") }) {
                    Text("[CORE]", color = Color(0xFF00FF88))
                }
            }
        }
    ) { padding ->
        NavHost(navController, "player", Modifier.padding(padding)) {
            composable("player") { 
                PlayerScreen(viewModel = playerVm, onOpenLibrary = { navController.navigate("library") }) 
            }
            composable("library") { 
                LibraryScreen(libraryVm) { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                } 
            }
            composable("eq") { EQScreen(eqVm) }
        }
    }
}
