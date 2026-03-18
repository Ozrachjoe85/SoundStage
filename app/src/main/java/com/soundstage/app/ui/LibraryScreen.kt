package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soundstage.app.data.models.Track
import com.soundstage.app.viewmodel.LibraryViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = viewModel(),
    onTrackSelected: (Track) -> Unit
) {
    val songs by viewModel.songs.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()
    
    val rackBg = Color(0xFF0D0F12)
    val panelBg = Color(0xFF1A1D23)
    val accentGreen = Color(0xFF00FF88)
    val accentAmber = Color(0xFFFFB000)
    
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
            // Header
            LibraryHeader(
                totalTracks = songs.size,
                isScanning = isScanning,
                onScan = { viewModel.scanLibrary() },
                accentColor = accentGreen
            )
            
            // Track List
            TrackListPanel(
                tracks = songs,
                onTrackSelected = onTrackSelected,
                accentColor = accentGreen
            )
        }
    }
}

@Composable
fun LibraryHeader(
    totalTracks: Int,
    isScanning: Boolean,
    onScan: () -> Unit,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LIBRARY DATABASE",
                    color = Color(0xFF6B7280),
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRACKS:",
                        color = Color(0xFF9AA4AD),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                            .background(Color(0xFF000000), RoundedCornerShape(2.dp))
                            .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp))
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = totalTracks.toString().padStart(5, '0'),
                            color = accentColor,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Scan button
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 40.dp)
                    .background(
                        if (isScanning) accentColor.copy(alpha = 0.2f)
                        else Color(0xFF2A2F36),
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        2.dp,
                        if (isScanning) accentColor else Color(0xFF4A5057),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = !isScanning, onClick = onScan),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isScanning) "SCAN..." else "SCAN",
                    color = if (isScanning) accentColor else Color(0xFF9AA4AD),
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TrackListPanel(
    tracks: List<Track>,
    onTrackSelected: (Track) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Color(0xFF1A1D23))
            .border(1.dp, Color(0xFF2A2F36))
            .padding(16.dp)
    ) {
        Text(
            text = "CATALOG ENTRIES",
            color = Color(0xFF6B7280),
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (tracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "NO DATA",
                        color = Color(0xFF6B7280),
                        fontSize = 14.sp,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Press SCAN to index library",
                        color = Color(0xFF4A5057),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0D0F12), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tracks) { track ->
                    TrackItem(
                        track = track,
                        onSelect = { onTrackSelected(track) },
                        accentColor = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    onSelect: () -> Unit,
    accentColor: Color
) {
    var isHovered by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                if (isHovered) Color(0xFF1A1D23) else Color.Transparent,
                RoundedCornerShape(2.dp)
            )
            .border(
                1.dp,
                if (isHovered) accentColor.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(2.dp)
            )
            .clickable {
                isHovered = !isHovered
                onSelect()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Track number
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(32.dp)
                .background(Color(0xFF000000), RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFF2A2F36), RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = track.id.toString().padStart(3, '0'),
                color = accentColor.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Track info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = track.title,
                color = if (isHovered) accentColor else Color(0xFF9AA4AD),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = track.artist,
                    color = Color(0xFF6B7280),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Text(
                    text = "•",
                    color = Color(0xFF4A5057),
                    fontSize = 11.sp
                )
                
                Text(
                    text = formatDuration(track.duration),
                    color = Color(0xFF6B7280),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        
        // Play indicator
        if (isHovered) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                    .border(1.dp, accentColor, RoundedCornerShape(2.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "►",
                    color = accentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", mins, secs)
}
