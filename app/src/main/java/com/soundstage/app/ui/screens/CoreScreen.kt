package com.soundstage.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.viewmodel.CoreViewModel
import kotlin.math.*

@Composable
fun CoreScreen(
    viewModel: CoreViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    val bg = Color(0xFF0A0C0F)
    val accent = Color(0xFF6CFFB0)
    val amber = Color(0xFFFFB347)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        Text(
            text = "CORE ENGINE",
            color = accent,
            fontSize = 14.sp,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            RetroKnob(
                label = "BASS",
                value = state.bass,
                onChange = viewModel::setBass,
                color = accent
            )

            RetroKnob(
                label = "MID",
                value = state.mid,
                onChange = viewModel::setMid,
                color = accent
            )

            RetroKnob(
                label = "TREBLE",
                value = state.treble,
                onChange = viewModel::setTreble,
                color = accent
            )

            RetroKnob(
                label = "WIDTH",
                value = state.width,
                onChange = viewModel::setWidth,
                color = amber
            )
        }
    }
}

@Composable
fun RetroKnob(
    label: String,
    value: Float,
    onChange: (Float) -> Unit,
    color: Color
) {

    val animatedValue by animateFloatAsState(targetValue = value)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(110.dp)
                .shadow(20.dp, CircleShape)
                .background(Color(0xFF151A20), CircleShape)
                .border(1.dp, Color(0xFF2A2F36), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val newValue = (value - dragAmount.y / 300f).coerceIn(0f, 1f)
                        onChange(newValue)
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            // glow layer
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(20.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(color.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )

            // indicator
            Canvas(modifier = Modifier.fillMaxSize()) {
                val angle = animatedValue * 270f - 135f
                val radius = size.minDimension / 2.5f

                val x = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

                drawLine(
                    color = color,
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 6f
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = label,
            color = Color(0xFF9AA4AD),
            fontSize = 12.sp,
            letterSpacing = 1.5.sp
        )

        Text(
            text = (animatedValue * 100).toInt().toString(),
            color = color,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
