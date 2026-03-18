package com.soundstage.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.soundstage.app.navigation.NavGraph // Resolved
import com.soundstage.app.ui.modifiers.CRTOverlay
import com.soundstage.app.ui.theme.SoundStageTheme

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundStageTheme {
                LaunchedEffect(Unit) {
                    val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
                    }
                    permissionLauncher.launch(permissions.toTypedArray())
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                    CRTOverlay()
                }
            }
        }
    }
}
