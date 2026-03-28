package com.soniclab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.soniclab.app.audio.EQMode
import com.soniclab.app.audio.IntelligentEQManager
import com.soniclab.app.ui.theme.sonicColors
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val eqManager: IntelligentEQManager
) : ViewModel() {
    
    val mode = eqManager.mode
    val isEnabled = eqManager.isEnabled
    val currentProfile = eqManager.currentProfile
    
    private val _manualLevels = mutableStateListOf(0f, 0f, 0f, 0f, 0f)
    val manualLevels: List<Float> = _manualLevels
    
    fun setMode(newMode: EQMode) {
        eqManager.setMode(newMode)
    }
    
    fun toggleEnabled() {
        eqManager.setEnabled(!isEnabled.value)
    }
    
    fun setManualBandLevel(bandIndex: Int, level: Float) {
        _manualLevels[bandIndex] = level
        eqManager.setManualBandLevel(bandIndex, level)
    }
    
    fun applyPreset(preset: String) {
        val levels = when (preset) {
            "Flat" -> listOf(0f, 0f, 0f, 0f, 0f)
            "Bass Boost" -> listOf(0.8f, 0.5f, 0f, 0f, 0f)
            "Treble Boost" -> listOf(0f, 0f, 0f, 0.6f, 0.8f)
            "Vocal" -> listOf(0f, 0f, 0.5f, 0.5f, 0f)
            "Rock" -> listOf(0.6f, 0.2f, 0f, 0.3f, 0.5f)
            else -> listOf(0f, 0f, 0f, 0f, 0f)
        }
        
        levels.forEachIndexed { index, level ->
            setManualBandLevel(index, level)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    modifier: Modifier = Modifier,
    viewModel: EqualizerViewModel = hiltViewModel()
) {
    val colors = sonicColors
    val scrollState = rememberScrollState()
    
    val mode by viewModel.mode.collectAsState()
    val isEnabled by viewModel.isEnabled.collectAsState()
    val currentProfile by viewModel.currentProfile.collectAsState()
    
    val bandNames = listOf("60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz")
    val presets = listOf("Flat", "Bass", "Treble", "Vocal", "Rock")
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("EQUALIZER", color = colors.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Switch(checked = isEnabled, onCheckedChange = { viewModel.toggleEnabled() })
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text("MODE", color = colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = mode == EQMode.AUTO,
                    onClick = { viewModel.setMode(EQMode.AUTO) },
                    label = { Text("AUTO", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = mode == EQMode.MANUAL,
                    onClick = { viewModel.setMode(EQMode.MANUAL) },
                    label = { Text("MANUAL", fontSize = 13.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        if (mode == EQMode.AUTO) {
            Text("STATUS", color = colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
                Column(Modifier.padding(16.dp)) {
                    currentProfile?.let { p ->
                        Text("Status: ${if (p.isLearned) "OPTIMIZED ✓" else "LEARNING..."}", fontSize = 14.sp)
                        Text("Plays: ${p.playCount}", fontSize = 14.sp)
                        Text("Confidence: ${(p.confidence * 100).toInt()}%", fontSize = 14.sp)
                        LinearProgressIndicator(p.confidence, Modifier.fillMaxWidth())
                    } ?: Text("No song playing", fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        
        if (mode == EQMode.MANUAL) {
            Text("PRESETS", color = colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    presets.forEach { preset ->
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.applyPreset(preset) },
                            label = { Text(preset, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
        
        Text(if (mode == EQMode.AUTO) "LEARNED EQ" else "MANUAL EQ", color = colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        
        Card(colors = CardDefaults.cardColors(containerColor = colors.surface)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (mode == EQMode.AUTO) {
                    currentProfile?.let { p ->
                        val levels = listOf(p.eq60Hz, p.eq230Hz, p.eq910Hz, p.eq3_6kHz, p.eq14kHz)
                        bandNames.forEachIndexed { i, band ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(band, fontSize = 14.sp)
                                Text("${(levels[i] * 10).toInt()}dB", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } ?: Text("Play a song to see learned EQ", fontSize = 14.sp)
                } else {
                    bandNames.forEachIndexed { i, band ->
                        Column {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(band, fontSize = 14.sp)
                                Text("${(viewModel.manualLevels[i] * 10).toInt()}dB", fontSize = 14.sp)
                            }
                            Slider(
                                value = (viewModel.manualLevels[i] + 1f) / 2f,
                                onValueChange = { viewModel.setManualBandLevel(i, it * 2f - 1f) },
                                enabled = isEnabled
                            )
                        }
                    }
                }
            }
        }
    }
}
