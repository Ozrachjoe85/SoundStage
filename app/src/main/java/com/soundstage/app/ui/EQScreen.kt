package com.soundstage.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.viewmodel.EQViewModel
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun EQScreen(
    viewModel: EQViewModel = viewModel()
) {
    val bands by viewModel.bands.collectAsState()
    
    val rackBg = Color(0xFF0D0F12)
    val accentGreen = Color(0xFF00FF88)
    val accentBlue = Color(0xFF00F0FF)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(rackBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Spectrum Analyzer
            SpectrumAnalyzerPanel(accentColor = accentGreen)
            
            // Frequency Bands
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1D23))
                    .border(1.dp, Color(0xFF2A2F36))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "8-BAND PARAMETRIC EQUALIZER",
                    color = Color(0xFF6B7280),
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val frequencies = listOf("60", "150", "400", "1k", "2.4k", "6k", "10k", "16k")
                    
                    bands.take(8).forEachIndexed { index, band ->
                        MiniVerticalSlider(
                            label = frequencies.getOrElse(index) { "${index + 1}" },
                            value = band.gain,
                            onChange = { /* viewModel.setBandGain(index, it) */ },
                            accentColor = accentBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpectrumAnalyzerPanel(accentColor: Color) {
    val spectrumData = remember { mutableStateListOf<Float>() }
    
    LaunchedEffect(Unit) {
        repeat(32) { spectrumData.add(0f) }
        
        while (true) {
            kotlinx.coroutines.delay(50)
            for (i in spectrumData.indices) {
                val baseValue = sin((i * 0.3f) + (System.currentTimeMillis() / 500f)) * 0.5f + 0.5f
                val noise = Random.nextFloat() * 0.2f
                spectrumData[i] = (baseValue + noise).coerceIn(0f, 1f)
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "REAL-TIME SPECTRUM ANALYZER",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFF000000), RoundedCornerShape(4.dp))
                .border(2.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / spectrumData.size
                val maxHeight = size.height - 40.dp.toPx()
                
                for (i in 0..5) {
                    val y = (size.height / 5) * i
                    drawLine(
                        color = Color(0xFF1A1D23),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }
                
                spectrumData.forEachIndexed { index, value ->
                    val x = index * barWidth
                    val barHeight = maxHeight * value
                    val y = size.height - barHeight
                    
                    val barColor = when {
                        value > 0.8f -> Color(0xFFFF0033)
                        value > 0.6f -> Color(0xFFFFB000)
                        else -> accentColor
                    }
                    
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                barColor,
                                barColor.copy(alpha = 0.3f)
                            ),
                            startY = y,
                            endY = size.height
                        ),
                        topLeft = Offset(x + 1.dp.toPx(), y),
                        size = Size(barWidth - 2.dp.toPx(), barHeight)
                    )
                }
            }
        }
    }
}

@Composable
fun MiniVerticalSlider(
    label: String,
    value: Float,
    onChange: (Float) -> Unit,
    accentColor: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "mini"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(80.dp)
                .background(Color(0xFF0D0F12), RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val position = change.position.y
                        val newValue = 1f - (position / size.height).coerceIn(0f, 1f)
                        onChange(newValue)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(animatedValue)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                accentColor,
                                accentColor.copy(alpha = 0.5f)
                            )
                        ),
                        RoundedCornerShape(2.dp)
                    )
            )
        }
        
        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
