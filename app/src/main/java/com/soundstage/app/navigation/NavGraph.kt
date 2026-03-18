package com.soundstage.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.PlayerScreen
import com.soundstage.app.ui.LibraryScreen
import com.soundstage.app.viewmodel.PlayerViewModel
import com.soundstage.app.viewmodel.LibraryViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "player") {
        composable("player") {
            // Standardizing on PlayerViewModel to fix the mismatch
            val playerVm: PlayerViewModel = viewModel()
            PlayerScreen(viewModel = playerVm)
        }
        
        composable("library") {
            val libraryVm: LibraryViewModel = viewModel()
            LibraryScreen(viewModel = libraryVm) { songId ->
                // Navigation logic for song selection
                navController.navigate("player")
            }
        }
    }
}
