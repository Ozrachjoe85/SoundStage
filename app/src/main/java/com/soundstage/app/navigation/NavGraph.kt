package com.soundstage.app.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.viewmodel.PlayerViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.soundstage.app.ui.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "library") {

        composable("library") {
            LibraryScreen(navController)
        }

        
        composable("player") {
    val viewModel: PlayerViewModel = viewModel() 
    PlayerScreen(viewModel = viewModel)
}

        composable("eq") {
            EqualizerScreen()
        }
    }
}
