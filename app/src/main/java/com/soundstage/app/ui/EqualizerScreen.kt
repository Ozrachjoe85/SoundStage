package com.soundstage.app.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundstage.app.audio.EQBand
import com.soundstage.app.audio.EqualizerBands

@Composable
fun EqualizerScreen() {
    // FIX: Removed the argument '16' to match our EqualizerBands object definition
    var bands by remember {
        mutableStateOf(EqualizerBands.createBands())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "AutoEQ Live",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary // Uses our Phosphor Green
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bands.size) { i ->
                val band = bands[i]

                // Smoothly animate the sliders when AutoEQ is processing
                val animatedGain by animateFloatAsState(
                    targetValue = band.gain,
                    animationSpec = tween(150),
                    label = "BandAnimation"
                )

                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Vertical slider setup for that "Analog Console" look
                    Slider(
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer {
                                rotationZ = 270f // Rotates to vertical
                            },
                        value = animatedGain,
                        onValueChange = { newValue ->
                            // Update the specific band in our list
                            bands = bands.toMutableList().apply {
                                this[i] = band.copy(gain = newValue)
                            }
                        },
                        valueRange = -10f..10f
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "${band.frequency.toInt()}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Hz",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
