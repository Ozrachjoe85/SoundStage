package com.soundstage.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundstage.app.viewmodel.EQViewModel

@Composable
fun EQScreen(viewModel: EQViewModel) {
    val bands by viewModel.bands.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A0B)).padding(16.dp)
    ) {
        Text("NEURAL_EQ // REAL-TIME ANALYSIS", color = Color(0xFF00FF88), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            bands.forEachIndexed { index, band ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.weight(1f).width(40.dp), contentAlignment = Alignment.BottomCenter) {
                        // The "Auto-EQ" Ghost Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight(band.autoOffset.coerceIn(0.1f, 1f))
                                .background(Color(0xFF00FF88).copy(alpha = 0.2f))
                        )
                        
                        // The Manual Slider (Rotated vertical)
                        Slider(
                            value = band.manualGain,
                            onValueChange = { viewModel.setManualGain(index, it) },
                            modifier = Modifier.align(Alignment.Center).fillMaxHeight().width(200.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF00FF88),
                                activeTrackColor = Color(0xFF004422)
                            )
                        )
                    }
                    Text(band.frequency, color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
