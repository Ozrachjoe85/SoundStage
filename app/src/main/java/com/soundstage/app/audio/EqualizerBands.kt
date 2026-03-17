package com.soundstage.app.audio

data class EQBand(
    val frequency: Float,
    var gain: Float = 0f
)

object EqualizerBands {

    fun createBands(count: Int = 32): List<EQBand> {
        val bands = mutableListOf<EQBand>()

        val minFreq = 20f
        val maxFreq = 20000f

        for (i in 0 until count) {
            val fraction = i.toFloat() / count
            val freq = minFreq * (maxFreq / minFreq).pow(fraction)
            bands.add(EQBand(freq))
        }

        return bands
    }
}
