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
import kotlinx.coroutines.delay

@Composable
fun EqualizerScreen() {

    var bands by remember {
        mutableStateOf(EqualizerBands.createBands(16))
    }

    // Fake animation loop (will hook real audio next)
    LaunchedEffect(Unit) {
        while (true) {
            bands = bands.map {
                it.copy(gain = (-5..5).random().toFloat())
            }
            delay(120)
        }
    }

    Column {
        Text("AutoEQ Live")

        LazyRow {
            items(bands.size) { i ->
                val band = bands[i]

                val animatedGain by animateFloatAsState(
                    targetValue = band.gain,
                    animationSpec = tween(100),
                    label = ""
                )

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Slider(
                        value = animatedGain,
                        onValueChange = { },
                        valueRange = -10f..10f
                    )
                    Text("${band.frequency.toInt()} Hz")
                }
            }
        }
    }
}
