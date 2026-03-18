package com.soundstage.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.PlayerScreen
import com.soundstage.app.ui.theme.SoundStageTheme

class MainActivity : ComponentActivity() {

    // Helper to request all necessary permissions for an Audio App
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            // Permissions granted: The MusicRepository will now be able to scan files
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define which permissions we need based on Android Version
        val permissionsToRequest = mutableListOf<String>()
        
        // Notification permission for the Playback Service (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            // Legacy storage permission for older Androids
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        setContent {
            SoundStageTheme {
                // Launch permission request once the UI starts
                LaunchedEffect(Unit) {
                    permissionLauncher.launch(permissionsToRequest.toTypedArray())
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.ui.graphics.Color(0xFF121212) // Charcoal Base
                ) {
                    // This initializes the ViewModel and the Player UI
                    PlayerScreen(viewModel = viewModel())
                }
            }
        }
    }
}
