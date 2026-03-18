package com.soundstage.app.navigation

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.soundstage.app.ui.*
import com.soundstage.app.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    
    // Scoping these ViewModels here ensures they persist during navigation
    val playerVm: PlayerViewModel = viewModel()
    val libraryVm: LibraryViewModel = viewModel()
    val eqVm: EQViewModel = viewModel()

    Scaffold(
        bottomBar = {
            // Ana-Digital Bottom Control Deck
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A0A0B))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DeckButton("DECK", active = true) { navController.navigate("player") }
                DeckButton("DATA", active = false) { navController.navigate("library") }
                DeckButton("CORE", active = false) { navController.navigate("eq") }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "player",
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFF0A0A0B))
        ) {
            // Route 1: The Playback Deck
            composable("player") {
                PlayerScreen(
                    viewModel = playerVm,
                    onOpenLibrary = { navController.navigate("library") }
                )
            }

            // Route 2: The Music Library (Data)
            composable("library") {
                LibraryScreen(viewModel = libraryVm) { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                }
            }

            // Route 3: The Neural EQ (Core)
            composable("eq") {
                EQScreen(viewModel = eqVm)
            }
        }
    }
}

@Composable
fun DeckButton(
    label: String, 
    active: Boolean, 
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "[$label]",
                color = if (active) Color(0xFF00FF88) else Color(0xFF004422),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )
            if (active) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 20.dp, height = 2.dp)
                        .background(Color(0xFF00FF88))
                )
            }
        }
    }
}
