package com.soundstage.app.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun PlayerScreen(navController: NavController) {
    Column {
        Text("Now Playing")
        Button(onClick = { navController.navigate("eq") }) {
            Text("Open EQ")
        }
    }
}
