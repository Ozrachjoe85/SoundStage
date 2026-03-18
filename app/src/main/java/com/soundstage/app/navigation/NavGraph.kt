package com.soundstage.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.PlayerScreen
import com.soundstage.app.ui.LibraryScreen
import com.soundstage.app.ui.AudioViewModel
import com.soundstage.app.viewmodel.LibraryViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "player") {
        composable("player") {
            val audioVm: AudioViewModel = viewModel()
            PlayerScreen(viewModel = audioVm)
        }
        composable("library") {
            val libraryVm: LibraryViewModel = viewModel()
            LibraryScreen(viewModel = libraryVm) { songId ->
                // Handle song selection logic here
            }
        }
    }
}
