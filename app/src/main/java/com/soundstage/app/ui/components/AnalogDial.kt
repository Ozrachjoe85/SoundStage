package com.soundstage.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun AnalogDial(label: String, onValueChange: (Float) -> Unit = {}) {
    var angle by remember { mutableFloatStateOf(-120f) } // Initial "7 o'clock" position

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    brush = Brush.radialGradient(listOf(Color(0xFF222222), Color(0xFF0A0A0B))),
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val dragVector = change.position - Offset(size.width / 2f, size.height / 2f)
                        val newAngle = Math.toDegrees(atan2(dragVector.y.toDouble(), dragVector.x.toDouble())).toFloat()
                        // Constrain rotation to standard knob range
                        if (newAngle in -150.0..150.0) {
                            angle = newAngle
                            val normalizedValue = (angle + 150f) / 300f
                            onValueChange(normalizedValue)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2
                val lineLength = radius * 0.8f
                val rad = (angle + 90) * (PI / 180).toFloat()
                
                drawLine(
                    color = Color(0xFF00FF88),
                    start = center,
                    end = Offset(
                        x = center.x + lineLength * kotlin.math.cos(rad).toFloat(),
                        y = center.y + lineLength * kotlin.math.sin(rad).toFloat()
                    ),
                    strokeWidth = 6f
                )
            }
        }
        Text(label, color = Color(0xFF004422), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    }
}
