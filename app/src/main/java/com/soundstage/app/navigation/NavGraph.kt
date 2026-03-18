package com.soundstage.app.navigation

import androidx.compose.foundation.background
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
import com.soundstage.app.ui.theme.SoundStageThemeManager
import com.soundstage.app.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val theme = SoundStageThemeManager.currentTheme
    val playerVm: PlayerViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = theme.background) {
                listOf("player", "library", "eq", "settings").forEach { route ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(route) },
                        icon = { Text(route.take(1).uppercase(), color = theme.primary, fontFamily = FontFamily.Monospace) },
                        label = { Text(route.uppercase(), color = theme.primary, fontSize = 8.sp) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, "player", Modifier.padding(padding)) {
            composable("player") { 
                PlayerScreen(playerVm) { navController.navigate("library") } 
            }
            composable("library") { 
                val libVm: LibraryViewModel = viewModel()
                LibraryScreen(libVm) { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                } 
            }
            composable("eq") { EQScreen(viewModel()) }
            composable("settings") { SettingsScreen() }
        }
    }
}
