package com.soundstage.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.soundstage.app.ui.theme.SoundStageThemeManager
import com.soundstage.app.viewmodel.PlayerViewModel

@Composable
fun HifiWaveform(viewModel: PlayerViewModel) {
    val theme = SoundStageThemeManager.currentTheme
    val magnitudes by viewModel.visualizerData.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / (magnitudes.size * 2)
        val centerY = size.height / 2

        magnitudes.forEachIndexed { index, mag ->
            val height = (mag * 2.5f).coerceIn(4f, centerY)
            
            // Draw Top (Normal)
            drawRect(
                color = theme.primary,
                topLeft = Offset(size.width / 2 + (index * barWidth), centerY - height),
                size = Size(barWidth - 4f, height)
            )
            drawRect(
                color = theme.primary,
                topLeft = Offset(size.width / 2 - (index * barWidth), centerY - height),
                size = Size(barWidth - 4f, height)
            )

            // Draw Bottom (Mirror Reflection)
            drawRect(
                color = theme.primary.copy(alpha = 0.3f),
                topLeft = Offset(size.width / 2 + (index * barWidth), centerY),
                size = Size(barWidth - 4f, height / 2)
            )
            drawRect(
                color = theme.primary.copy(alpha = 0.3f),
                topLeft = Offset(size.width / 2 - (index * barWidth), centerY),
                size = Size(barWidth - 4f, height / 2)
            )
        }
    }
}
