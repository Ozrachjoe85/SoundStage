package com.soundstage.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.*
import com.soundstage.app.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    // By creating the PlayerViewModel here, it persists across screen swaps
    val playerVm: PlayerViewModel = viewModel()

    NavHost(navController = navController, startDestination = "player") {
        composable("player") {
            PlayerScreen(
                viewModel = playerVm,
                onOpenLibrary = { navController.navigate("library") }
            )
        }
        
        composable("library") {
            val libraryVm: LibraryViewModel = viewModel()
            LibraryScreen(
                viewModel = libraryVm,
                onSongClick = { song ->
                    playerVm.loadAndPlay(song)
                    navController.navigate("player")
                }
            )
        }
    }
}
