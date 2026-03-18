package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.viewmodel.EQViewModel

@Composable
fun EQScreen(viewModel: EQViewModel) {
    val bands by viewModel.bands.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0B)).padding(20.dp)
    ) {
        Text("NEURAL_ENGINE // PARAMETRIC_EQ", color = Color(0xFF00FF88), fontSize = 14.sp, fontFamily = FontFamily.Monospace)
        Text("STATUS: OPTIMIZING_OUTPUT", color = Color(0xFF004422), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bands.forEachIndexed { index, band ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier.weight(1f).width(40.dp).background(Color(0xFF050505)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // The "Auto-EQ" Ghost Indicator (Phosphor Glow)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .fillMaxHeight(band.autoOffset)
                                .background(Color(0xFF00FF88).copy(alpha = 0.15f))
                        )

                        // Vertical Slider Logic
                        Slider(
                            value = band.manualGain,
                            onValueChange = { 
                                viewModel.setManualGain(index, it)
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            },
                            modifier = Modifier.fillMaxHeight().width(20.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF00FF88),
                                activeTrackColor = Color(0xFF00FF88).copy(alpha = 0.2f),
                                inactiveTrackColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(band.frequency, color = Color.Gray, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
