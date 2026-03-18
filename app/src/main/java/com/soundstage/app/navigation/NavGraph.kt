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
    val playerVm: PlayerViewModel = viewModel()
    val libraryVm: LibraryViewModel = viewModel()
    val eqVm: EQViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF0A0A0B)).padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DeckNavItem("DECK", active = currentRoute == "player") { navController.navigate("player") }
                DeckNavItem("DATA", active = currentRoute == "library") { navController.navigate("library") }
                DeckNavItem("CORE", active = currentRoute == "eq") { navController.navigate("eq") }
            }
        }
    ) { padding ->
        NavHost(navController, "player", Modifier.padding(padding)) {
            composable("player") { PlayerScreen(playerVm) { navController.navigate("library") } }
            composable("library") { LibraryScreen(libraryVm) { song ->
                playerVm.loadAndPlay(song)
                navController.navigate("player")
            } }
            composable("eq") { EQScreen(eqVm) }
        }
    }
}

@Composable
fun DeckNavItem(label: String, active: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            "[$label]",
            color = if (active) Color(0xFF00FF88) else Color(0xFF004422),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}
