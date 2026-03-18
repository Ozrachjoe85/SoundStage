package com.soundstage.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
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
    val isAutoEqEnabled by viewModel.isAutoEqEnabled.collectAsState()
    
    val rackBg = Color(0xFF0D0F12)
    val accentGreen = Color(0xFF00FF88)
    val accentAmber = Color(0xFFFFB000)
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
            
            // AutoEQ Controls
            AutoEqControlPanel(
                isEnabled = isAutoEqEnabled,
                onToggle = { viewModel.toggleAutoEq() },
                accentColor = accentAmber
            )
            
            // Frequency Bands
            FrequencyBandsPanel(
                bands = bands,
                onBandChange = { index, value ->
                    viewModel.setBand(index, value)
                },
                accentColor = accentBlue
            )
        }
    }
}

@Composable
fun SpectrumAnalyzerPanel(accentColor: Color) {
    // Simulated spectrum data with animation
    val spectrumData = remember { mutableStateListOf<Float>() }
    
    LaunchedEffect(Unit) {
        // Initialize with 32 frequency bands
        repeat(32) { spectrumData.add(0f) }
        
        // Animate spectrum
        while (true) {
            kotlinx.coroutines.delay(50)
            for (i in spectrumData.indices) {
                // Simulate audio frequency response
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
                
                // Draw grid lines
                for (i in 0..5) {
                    val y = (size.height / 5) * i
                    drawLine(
                        color = Color(0xFF1A1D23),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }
                
                // Draw spectrum bars
                spectrumData.forEachIndexed { index, value ->
                    val x = index * barWidth
                    val barHeight = maxHeight * value
                    val y = size.height - barHeight
                    
                    // Bar gradient based on intensity
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
                
                // Draw frequency labels
                val frequencies = listOf("60", "250", "1k", "4k", "16k")
                frequencies.forEachIndexed { index, freq ->
                    val x = (size.width / (frequencies.size - 1)) * index
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            freq,
                            x,
                            size.height - 10.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.parseColor("#6B7280")
                                textSize = 20f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }
        
        // Peak indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PeakIndicator(label = "BASS", level = 0.7f, color = accentColor)
            PeakIndicator(label = "MID", level = 0.5f, color = accentColor)
            PeakIndicator(label = "HIGH", level = 0.6f, color = accentColor)
        }
    }
}

@Composable
fun PeakIndicator(label: String, level: Float, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(10) { index ->
                Box(
                    modifier = Modifier
                        .size(width = 8.dp, height = 20.dp)
                        .background(
                            if ((index / 10f) < level) {
                                when {
                                    index >= 8 -> Color(0xFFFF0033)
                                    index >= 6 -> Color(0xFFFFB000)
                                    else -> color
                                }
                            } else {
                                Color(0xFF1A1D23)
                            },
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun AutoEqControlPanel(
    isEnabled: Boolean,
    onToggle: () -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "INTELLIGENT AUTO-EQ ENGINE",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "AUTO OPTIMIZATION",
                    color = Color(0xFF9AA4AD),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = if (isEnabled) "ACTIVE - ANALYZING" else "STANDBY",
                    color = if (isEnabled) accentColor else Color(0xFF5A6470),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            
            // Toggle switch
            ToggleSwitch(
                isOn = isEnabled,
                onToggle = onToggle,
                activeColor = accentColor
            )
        }
        
        if (isEnabled) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusIndicator(label = "ANALYZING", isActive = true, color = accentColor)
                StatusIndicator(label = "OPTIMIZING", isActive = true, color = accentColor)
                StatusIndicator(label = "LIMITING", isActive = false, color = Color(0xFF5A6470))
            }
        }
    }
}

@Composable
fun ToggleSwitch(
    isOn: Boolean,
    onToggle: () -> Unit,
    activeColor: Color
) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 40.dp)
            .background(
                if (isOn) activeColor.copy(alpha = 0.2f) else Color(0xFF0D0F12),
                RoundedCornerShape(20.dp)
            )
            .border(
                2.dp,
                if (isOn) activeColor else Color(0xFF2A2F36),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onToggle)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(if (isOn) Alignment.CenterEnd else Alignment.CenterStart)
                .background(
                    if (isOn) activeColor else Color(0xFF3A3F47),
                    CircleShape
                )
        )
    }
}

@Composable
fun StatusIndicator(label: String, isActive: Boolean, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    if (isActive) color else Color(0xFF2A2F36),
                    CircleShape
                )
        )
        
        Text(
            text = label,
            color = if (isActive) color else Color(0xFF5A6470),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun FrequencyBandsPanel(
    bands: List<Float>,
    onBandChange: (Int, Float) -> Unit,
    accentColor: Color
) {
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
            
            bands.forEachIndexed { index, value ->
                MiniVerticalSlider(
                    label = frequencies.getOrElse(index) { "${index + 1}" },
                    value = value,
                    onChange = { onBandChange(index, it) },
                    accentColor = accentColor
                )
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
            // Level fill
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
