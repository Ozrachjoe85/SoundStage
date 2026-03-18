package com.soundstage.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.soundstage.app.ui.theme.SoundStageTheme
import com.soundstage.app.ui.PlayerScreen
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundStageTheme {
                // This launches your new Retro-Digital UI
                PlayerScreen(viewModel = viewModel())
            }
        }
    }
}
