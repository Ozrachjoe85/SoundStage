package com.soundstage.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.soundstage.app.ui.*
import com.soundstage.app.ui.theme.TacticalThemeManager
import com.soundstage.app.viewmodel.*
import com.soundstage.app.data.Song

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val theme = TacticalThemeManager.currentTheme
    val playerVm: PlayerViewModel = viewModel()
    
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = theme.background, modifier = Modifier.height(60.dp)) {
                listOf("DECK", "DATA", "CORE", "CONF").forEach { label ->
                    val route = when(label) {
                        "DECK" -> "player"
                        "DATA" -> "library"
                        "CORE" -> "eq"
                        else -> "settings"
                    }
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(route) },
                        icon = { Text("[$label]", color = theme.primary, fontSize = 11.sp, fontFamily = FontFamily.Monospace) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, "player", Modifier.padding(padding)) {
            composable("player") { PlayerScreen(playerVm) { navController.navigate("library") } }
            composable("library") {
                LibraryScreen(viewModel(), onSongClick = { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                })
            }
            composable("eq") { EQScreen(viewModel()) }
            composable("settings") { SettingsScreen() }
        }
    }
}
