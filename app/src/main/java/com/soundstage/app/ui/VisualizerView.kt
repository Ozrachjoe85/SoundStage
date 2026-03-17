package com.soundstage.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawLine
import androidx.compose.ui.geometry.Offset

@Composable
fun VisualizerView(data: FloatArray, modifier: Modifier = Modifier) {

    Canvas(modifier = modifier) {

        val width = size.width
        val height = size.height

        val step = width / data.size

        data.forEachIndexed { i, value ->
            val x = i * step
            val y = height / 2 + value * height / 2

            drawLine(
                start = Offset(x, height / 2),
                end = Offset(x, y)
            )
        }
    }
}
