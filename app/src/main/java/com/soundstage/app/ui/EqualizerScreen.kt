package com.soundstage.app.ui

import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundstage.app.audio.EqualizerBands

@Composable
fun EqualizerScreen() {

    var bands by remember {
        mutableStateOf(EqualizerBands.createBands(16)) // simple mode default
    }

    Column {
        Text("AutoEQ (Live)")

        LazyRow {
            items(bands.size) { i ->
                val band = bands[i]

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Slider(
                        value = band.gain,
                        onValueChange = {
                            band.gain = it
                        },
                        valueRange = -10f..10f
                    )
                    Text("${band.frequency.toInt()} Hz")
                }
            }
        }
    }
}
