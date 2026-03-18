package com.soundstage.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class EQBand(val frequency: String, val manualGain: Float, val autoOffset: Float)

class EQViewModel : ViewModel() {
    // 7-Band setup: 60, 150, 400, 1k, 2.4k, 6k, 15k
    private val _bands = MutableStateFlow(
        listOf("60", "150", "400", "1k", "2.4k", "6k", "15k").map { 
            EQBand(it, 0.5f, 0f) 
        }
    )
    val bands: StateFlow<List<EQBand>> = _bands

    // This simulates the "Auto-EQ" logic adjusting to the music
    fun updateAutoOffsets(newOffsets: FloatArray) {
        val current = _bands.value.toMutableList()
        newOffsets.forEachIndexed { i, offset ->
            if (i < current.size) {
                current[i] = current[i].copy(autoOffset = offset)
            }
        }
        _bands.value = current
    }

    fun setManualGain(index: Int, gain: Float) {
        val current = _bands.value.toMutableList()
        current[index] = current[index].copy(manualGain = gain)
        _bands.value = current
    }
}
