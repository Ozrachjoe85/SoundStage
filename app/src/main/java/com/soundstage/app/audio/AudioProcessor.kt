package com.soundstage.app.audio

import kotlin.math.hypot

class AudioProcessor {
    /**
     * Processes raw FFT data into frequency magnitudes.
     * Fixes the "Unresolved reference: process" error.
     */
    fun process(fft: ByteArray): FloatArray {
        val n = fft.size
        val magnitudes = FloatArray(n / 2)
        for (i in 0 until n / 2) {
            val real = fft[i * 2].toFloat()
            val imag = fft[i * 2 + 1].toFloat()
            // Calculate magnitude: sqrt(re^2 + im^2)
            magnitudes[i] = hypot(real, imag)
        }
        return magnitudes
    }
}
