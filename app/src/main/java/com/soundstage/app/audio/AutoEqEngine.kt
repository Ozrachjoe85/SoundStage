package com.soundstage.app.audio

import com.soundstage.app.audio.EqualizerBands
import com.soundstage.app.audio.EQBand

class AutoEqEngine {

    // Ensure FFTProcessor is defined in your project or use a placeholder
    private val fft = FFTProcessor() 
    private var bands = EqualizerBands.createBands() // Removed the '32' argument to match the object definition

    fun process(audioData: FloatArray): List<EQBand> {
        val spectrum = fft.performFFT(audioData)

        // Explicitly cast to Float to avoid 'Overload resolution ambiguity'
        val bandSize = if (bands.isNotEmpty()) spectrum.size / bands.size else 1

        bands.forEachIndexed { index, band ->
            val start = index * bandSize
            val end = (start + bandSize).coerceAtMost(spectrum.size)

            var sum = 0f
            for (i in start until end) {
                sum += spectrum.getOrElse(i) { 0f }
            }

            // Ensure division uses Float for high-grade precision
            val avg = if (bandSize > 0) sum / bandSize.toFloat() else 0f

            // Update the existing band gain
            band.gain = normalize(avg)
        }

        return bands
    }

    private fun normalize(value: Float): Float {
        // Explicitly using 1000f and 0.5f prevents the 'div' ambiguity errors
        return ((value / 1000f).coerceIn(0f, 1f) - 0.5f) * 10f
    }
}
