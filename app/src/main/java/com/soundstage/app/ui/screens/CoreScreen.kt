package com.soundstage.app.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.ui.viewmodel.CoreViewModel
import kotlin.math.*

@Composable
fun CoreScreen(
    viewModel: CoreViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Theme colors
    val rackBg = Color(0xFF0D0F12)
    val panelBg = Color(0xFF1A1D23)
    val accentGreen = Color(0xFF00FF88)
    val accentAmber = Color(0xFFFFB000)
    val accentRed = Color(0xFFFF0033)
    val metalLight = Color(0xFF3A3F47)
    val metalDark = Color(0xFF1E2228)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(rackBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Section
            RackHeader()
            
            // Main EQ Panel
            EQRackPanel(
                bass = state.bass,
                mid = state.mid,
                treble = state.treble,
                onBassChange = viewModel::setBass,
                onMidChange = viewModel::setMid,
                onTrebleChange = viewModel::setTreble,
                accentColor = accentGreen
            )
            
            // Stereo Width Panel
            StereoRackPanel(
                width = state.width,
                onWidthChange = viewModel::setWidth,
                accentColor = accentAmber
            )
            
            // VU Meter Panel
            VUMeterPanel(
                bass = state.bass,
                mid = state.mid,
                treble = state.treble,
                accentColor = accentGreen,
                redlineColor = accentRed
            )
        }
    }
}

@Composable
fun RackHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "● REC",
            color = Color(0xFFFF0033),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "SOUNDSTAGE AUDIO PROCESSOR",
            color = Color(0xFF00FF88),
            fontSize = 13.sp,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LEDIndicator(isActive = true, color = Color(0xFF00FF88))
            LEDIndicator(isActive = true, color = Color(0xFFFFB000))
            LEDIndicator(isActive = false, color = Color(0xFFFF0033))
        }
    }
}

@Composable
fun LEDIndicator(isActive: Boolean, color: Color) {
    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .size(8.dp)
            .shadow(
                elevation = if (isActive) 8.dp else 0.dp,
                shape = CircleShape,
                ambientColor = color,
                spotColor = color
            )
            .background(
                if (isActive) color.copy(alpha = glowAlpha) else Color(0xFF2A2F36),
                CircleShape
            )
    )
}

@Composable
fun EQRackPanel(
    bass: Float,
    mid: Float,
    treble: Float,
    onBassChange: (Float) -> Unit,
    onMidChange: (Float) -> Unit,
    onTrebleChange: (Float) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Panel Label
        Text(
            text = "3-BAND PARAMETRIC EQUALIZER",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VerticalSliderWithMeter(
                label = "BASS",
                frequency = "80 Hz",
                value = bass,
                onChange = onBassChange,
                accentColor = accentColor
            )
            
            VerticalSliderWithMeter(
                label = "MID",
                frequency = "1.2 kHz",
                value = mid,
                onChange = onMidChange,
                accentColor = accentColor
            )
            
            VerticalSliderWithMeter(
                label = "TREBLE",
                frequency = "8 kHz",
                value = treble,
                onChange = onTrebleChange,
                accentColor = accentColor
            )
        }
    }
}

@Composable
fun VerticalSliderWithMeter(
    label: String,
    frequency: String,
    value: Float,
    onChange: (Float) -> Unit,
    accentColor: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "slider"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label
        Text(
            text = label,
            color = Color(0xFF9AA4AD),
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        // Frequency
        Text(
            text = frequency,
            color = Color(0xFF5A6470),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
        
        // Slider Track
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(200.dp)
                .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val position = change.position.y
                        val newValue = 1f - (position / size.height).coerceIn(0f, 1f)
                        onChange(newValue)
                    }
                }
        ) {
            // Level Indicators (like LED ladder)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                repeat(20) { index ->
                    val segmentValue = (19 - index) / 19f
                    val isActive = animatedValue >= segmentValue
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                when {
                                    !isActive -> Color(0xFF1A1D23)
                                    segmentValue > 0.8f -> Color(0xFFFF0033) // Red zone
                                    segmentValue > 0.6f -> Color(0xFFFFB000) // Amber zone
                                    else -> accentColor // Green zone
                                },
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
            
            // Slider Handle
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-animatedValue * 192).dp)
                    .size(width = 48.dp, height = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3A3F47),
                                Color(0xFF2A2F36)
                            )
                        ),
                        RoundedCornerShape(2.dp)
                    )
                    .border(1.dp, Color(0xFF4A5057), RoundedCornerShape(2.dp))
            )
        }
        
        // Digital Readout
        DigitalDisplay(
            value = ((animatedValue - 0.5f) * 24).toInt(),
            unit = "dB",
            accentColor = accentColor
        )
    }
}

@Composable
fun DigitalDisplay(value: Int, unit: String, accentColor: Color) {
    Row(
        modifier = Modifier
            .width(70.dp)
            .height(28.dp)
            .background(Color(0xFF000000), RoundedCornerShape(2.dp))
            .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%+3d", value),
            color = accentColor,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = unit,
            color = accentColor.copy(alpha = 0.7f),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun StereoRackPanel(
    width: Float,
    onWidthChange: (Float) -> Unit,
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
            text = "STEREO FIELD PROCESSOR",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Width label
            Column {
                Text(
                    text = "WIDTH",
                    color = Color(0xFF9AA4AD),
                    fontSize = 11.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "M/S BLEND",
                    color = Color(0xFF5A6470),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            
            // Horizontal Slider
            HorizontalMeterSlider(
                value = width,
                onChange = onWidthChange,
                accentColor = accentColor
            )
            
            // Digital readout
            DigitalDisplay(
                value = (width * 200 - 100).toInt(),
                unit = "%",
                accentColor = accentColor
            )
        }
    }
}

@Composable
fun HorizontalMeterSlider(
    value: Float,
    onChange: (Float) -> Unit,
    accentColor: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "slider"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val position = change.position.x
                    val newValue = (position / size.width).coerceIn(0f, 1f)
                    onChange(newValue)
                }
            }
    ) {
        // Background segments
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(40) { index ->
                val segmentValue = index / 39f
                val isActive = animatedValue >= segmentValue
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (isActive) accentColor else Color(0xFF1A1D23),
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }
        
        // Marker lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            drawLine(
                color = Color(0xFF4A5057),
                start = Offset(centerX, 0f),
                end = Offset(centerX, size.height),
                strokeWidth = 2f
            )
        }
    }
}

@Composable
fun VUMeterPanel(
    bass: Float,
    mid: Float,
    treble: Float,
    accentColor: Color,
    redlineColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "OUTPUT LEVEL MONITORING",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            VUMeter(label = "BASS", value = bass, accentColor = accentColor, redlineColor = redlineColor)
            VUMeter(label = "MID", value = mid, accentColor = accentColor, redlineColor = redlineColor)
            VUMeter(label = "TREBLE", value = treble, accentColor = accentColor, redlineColor = redlineColor)
        }
    }
}

@Composable
fun VUMeter(
    label: String,
    value: Float,
    accentColor: Color,
    redlineColor: Color
) {
    // Animate with decay for more realistic VU behavior
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 100f),
        label = "vu"
    )
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF9AA4AD),
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(60.dp)
        )
        
        // VU Meter with needle
        Box(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val centerY = height / 2
                val arcTop = centerY - 20.dp.toPx()
                val arcHeight = 40.dp.toPx()
                
                // Background arc
                drawArc(
                    color = Color(0xFF0D0F12),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(0f, arcTop),
                    size = Size(width, arcHeight),
                    style = Stroke(width = 3.dp.toPx())
                )
                
                // Scale marks
                for (i in 0..10) {
                    val angle = 180f + (i * 18f)
                    val rad = Math.toRadians(angle.toDouble())
                    val innerRadius = (width / 2) - 15.dp.toPx()
                    val outerRadius = (width / 2) - 5.dp.toPx()
                    
                    val x1 = (width / 2) + (innerRadius * cos(rad)).toFloat()
                    val y1 = centerY + (innerRadius * sin(rad)).toFloat()
                    val x2 = (width / 2) + (outerRadius * cos(rad)).toFloat()
                    val y2 = centerY + (outerRadius * sin(rad)).toFloat()
                    
                    drawLine(
                        color = if (i >= 8) redlineColor else Color(0xFF4A5057),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = if (i >= 8) 3f else 1.5f
                    )
                }
                
                // Needle
                val needleAngle = 180f + (animatedValue * 180f)
                val needleRad = Math.toRadians(needleAngle.toDouble())
                val needleLength = (width / 2) - 10.dp.toPx()
                
                val needleX = (width / 2) + (needleLength * cos(needleRad)).toFloat()
                val needleY = centerY + (needleLength * sin(needleRad)).toFloat()
                
                // Needle shadow
                drawLine(
                    color = Color.Black.copy(alpha = 0.3f),
                    start = Offset(width / 2 + 2.dp.toPx(), centerY + 2.dp.toPx()),
                    end = Offset(needleX + 2.dp.toPx(), needleY + 2.dp.toPx()),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                
                // Needle
                drawLine(
                    color = if (animatedValue > 0.8f) redlineColor else accentColor,
                    start = Offset(width / 2, centerY),
                    end = Offset(needleX, needleY),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                
                // Center pivot
                drawCircle(
                    color = Color(0xFF3A3F47),
                    radius = 4.dp.toPx(),
                    center = Offset(width / 2, centerY)
                )
                
                drawCircle(
                    color = Color(0xFF5A6470),
                    radius = 2.dp.toPx(),
                    center = Offset(width / 2, centerY)
                )
            }
        }
        
        // Peak indicator
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    if (animatedValue > 0.95f) redlineColor else Color(0xFF2A2F36),
                    CircleShape
                )
        )
    }
}
