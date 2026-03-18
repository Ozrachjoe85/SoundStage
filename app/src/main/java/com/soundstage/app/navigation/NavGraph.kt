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
    val playerVm: PlayerViewModel = viewModel()
    val libraryVm: LibraryViewModel = viewModel()
    val eqVm: EQViewModel = viewModel()
    
    val theme = SoundStageThemeManager.currentTheme

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(theme.background).padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavTab("DECK", "player", navController)
                NavTab("DATA", "library", navController)
                NavTab("CORE", "eq", navController)
                NavTab("CONF", "settings", navController)
            }
        }
    ) { padding ->
        NavHost(navController, "player", Modifier.padding(padding)) {
            composable("player") { 
                // FIXED: Explicitly passing both required navigation lambdas
                PlayerScreen(
                    viewModel = playerVm, 
                    onOpenLibrary = { navController.navigate("library") },
                    onOpenQueue = { navController.navigate("queue") }
                ) 
            }
            composable("library") { 
                LibraryScreen(libraryVm) { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                } 
            }
            composable("eq") { EQScreen(eqVm) }
            composable("settings") { SettingsScreen() }
            
            // Adding the placeholder for Queue so the navigation doesn't crash
            composable("queue") { 
                Box(Modifier.fillMaxSize().background(theme.background)) {
                    Text("QUEUE_MODULE_ACTIVE", color = theme.primary, modifier = Modifier.padding(20.dp))
                }
            }
        }
    }
}

@Composable
fun NavTab(label: String, route: String, navController: androidx.navigation.NavController) {
    val theme = SoundStageThemeManager.currentTheme
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val active = currentRoute == route

    TextButton(onClick = { navController.navigate(route) }) {
        Text(
            text = if (active) "[$label]" else label, 
            color = if (active) theme.primary else theme.accent, 
            fontFamily = FontFamily.Monospace, 
            fontSize = 11.sp
        )
    }
}
