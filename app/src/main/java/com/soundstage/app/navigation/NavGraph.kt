package com.soundstage.app.navigation

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
            PlayerScreen(navController)
        }

        composable("eq") {
            EqualizerScreen()
        }
    }
}
