package com.soundstage.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.viewmodel.PlayerViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = viewModel(),
    onNavigateToLibrary: () -> Unit
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()
    val position by viewModel.position.collectAsState()
    val duration by viewModel.duration.collectAsState()
    
    val rackBg = Color(0xFF0D0F12)
    val panelBg = Color(0xFF1A1D23)
    val accentGreen = Color(0xFF00FF88)
    val accentAmber = Color(0xFFFFB000)
    val accentRed = Color(0xFFFF0033)
    
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
            // Display Panel
            DisplayPanel(
                songTitle = currentSong?.title ?: "NO SIGNAL",
                artist = currentSong?.artist ?: "---",
                album = currentSong?.album ?: "---",
                accentColor = accentGreen
            )
            
            // Tape Reels
            TapeReelsPanel(
                isPlaying = isPlaying,
                accentColor = accentGreen
            )
            
            // Timeline
            TimelinePanel(
                position = position,
                duration = duration,
                accentColor = accentAmber
            )
            
            // Transport Controls
            TransportControlsPanel(
                isPlaying = isPlaying,
                onPlayPause = { viewModel.togglePlayPause() },
                onPrevious = { viewModel.previousTrack() },
                onNext = { viewModel.nextTrack() },
                onStop = { viewModel.stop() },
                onOpenLibrary = onNavigateToLibrary,
                accentColor = accentGreen,
                stopColor = accentRed
            )
        }
    }
}

@Composable
fun DisplayPanel(
    songTitle: String,
    artist: String,
    album: String,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "NOW PLAYING",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        // Main display (like a tape counter display)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF000000), RoundedCornerShape(4.dp))
                .border(2.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = songTitle,
                    color = accentColor,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = artist,
                    color = accentColor.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = album,
                    color = accentColor.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TapeReelsPanel(
    isPlaying: Boolean,
    accentColor: Color
) {
    // Infinite rotation when playing
    val infiniteTransition = rememberInfiniteTransition(label = "reel")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "TAPE TRANSPORT",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Reel
            TapeReel(
                rotation = if (isPlaying) rotation else 0f,
                accentColor = accentColor,
                label = "L"
            )
            
            // Tape head indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF0D0F12), CircleShape)
                        .border(2.dp, Color(0xFF2A2F36), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                if (isPlaying) accentColor else Color(0xFF2A2F36),
                                CircleShape
                            )
                    )
                }
                
                Text(
                    text = "HEAD",
                    color = Color(0xFF6B7280),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            
            // Right Reel
            TapeReel(
                rotation = if (isPlaying) -rotation else 0f,
                accentColor = accentColor,
                label = "R"
            )
        }
    }
}

@Composable
fun TapeReel(
    rotation: Float,
    accentColor: Color,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(8.dp, CircleShape)
                .background(Color(0xFF0D0F12), CircleShape)
                .border(3.dp, Color(0xFF2A2F36), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotation)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 20.dp.toPx()
                
                // Center hub
                drawCircle(
                    color = Color(0xFF2A2F36),
                    radius = radius * 0.3f,
                    center = center
                )
                
                // Spokes
                for (i in 0 until 6) {
                    val angle = (i * 60f) * (Math.PI / 180f)
                    val x1 = center.x + (radius * 0.3f * cos(angle)).toFloat()
                    val y1 = center.y + (radius * 0.3f * sin(angle)).toFloat()
                    val x2 = center.x + (radius * cos(angle)).toFloat()
                    val y2 = center.y + (radius * sin(angle)).toFloat()
                    
                    drawLine(
                        color = accentColor.copy(alpha = 0.3f),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 3f
                    )
                }
                
                // Outer ring
                drawCircle(
                    color = accentColor.copy(alpha = 0.2f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
        
        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TimelinePanel(
    position: Long,
    duration: Long,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeDisplay(time = position, label = "POSITION", accentColor = accentColor)
            TimeDisplay(time = duration - position, label = "REMAINING", accentColor = accentColor)
        }
        
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
        ) {
            val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
            
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.8f),
                                accentColor
                            )
                        ),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun TimeDisplay(
    time: Long,
    label: String,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF6B7280),
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
        
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(32.dp)
                .background(Color(0xFF000000), RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formatTime(time),
                color = accentColor,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun TransportControlsPanel(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onStop: () -> Unit,
    onOpenLibrary: () -> Unit,
    accentColor: Color,
    stopColor: Color
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
            text = "TRANSPORT CONTROLS",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous
            TransportButton(
                label = "◄◄",
                onClick = onPrevious,
                color = accentColor
            )
            
            // Stop
            TransportButton(
                label = "■",
                onClick = onStop,
                color = stopColor
            )
            
            // Play/Pause
            TransportButton(
                label = if (isPlaying) "❚❚" else "►",
                onClick = onPlayPause,
                color = accentColor,
                isActive = isPlaying
            )
            
            // Next
            TransportButton(
                label = "►►",
                onClick = onNext,
                color = accentColor
            )
            
            // Library
            TransportButton(
                label = "≡",
                onClick = onOpenLibrary,
                color = Color(0xFFFFB000)
            )
        }
    }
}

@Composable
fun TransportButton(
    label: String,
    onClick: () -> Unit,
    color: Color,
    isActive: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .shadow(
                elevation = if (isActive) 8.dp else 4.dp,
                shape = RoundedCornerShape(4.dp),
                ambientColor = if (isActive) color else Color.Black
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3A3F47),
                        Color(0xFF2A2F36)
                    )
                ),
                RoundedCornerShape(4.dp)
            )
            .border(
                2.dp,
                if (isActive) color else Color(0xFF4A5057),
                RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isActive) color else Color(0xFF9AA4AD),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
