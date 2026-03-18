package com.soundstage.app.ui.modifiers

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun CRTOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "crt")
    
    // Subtle brightness flicker
    val flicker by infiniteTransition.animateFloat(
        initialValue = 0.98f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(50, easing = LinearEasing), RepeatMode.Reverse)
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val scanlineCount = size.height.toInt() / 6
        
        // Draw static scanlines
        for (i in 0 until scanlineCount) {
            val y = i * 6f
            drawLine(
                color = Color.Black.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
        }

        // Draw the global flicker tint
        drawRect(color = Color.White.copy(alpha = 1f - flicker))
    }
}
