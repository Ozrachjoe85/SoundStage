package com.soundstage.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.soundstage.app.ui.theme.TacticalThemeManager
import com.soundstage.app.viewmodel.PlayerViewModel

@Composable
fun HifiWaveform(viewModel: PlayerViewModel) {
    val theme = TacticalThemeManager.currentTheme
    val magnitudes by viewModel.visualizerData.collectAsState()

    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        val barWidth = size.width / (magnitudes.size)
        val centerY = size.height / 2

        magnitudes.forEachIndexed { index, mag ->
            // Scale the magnitude for a more dramatic tactical readout
            val h = (mag * 3.0f).coerceIn(2f, centerY)
            
            // Redline check
            val color = if (h > centerY * 0.82f) theme.hazard else theme.primary

            // Draw symmetrical stack (Top & Bottom)
            drawRect(
                color = color,
                topLeft = Offset(index * barWidth, centerY - h),
                size = Size(barWidth - 4f, h * 2)
            )
            
            // Draw subtle "ghost" reflection
            drawRect(
                color = color.copy(alpha = 0.1f),
                topLeft = Offset(index * barWidth, centerY - (h * 1.2f)),
                size = Size(barWidth - 4f, h * 2.4f)
            )
        }
    }
}
