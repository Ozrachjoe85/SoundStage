package com.soundstage.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.soundstage.app.navigation.NavGraph
import com.soundstage.app.ui.theme.SoundStageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Permissions are assumed granted for this "Everything Wired" build
        setContent {
            SoundStageTheme {
                NavGraph()
            }
        }
    }
}
