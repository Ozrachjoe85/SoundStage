package com.soundstage.app.audio
import kotlin.math.sqrt

class FFTProcessor {
    fun performFFT(data: FloatArray): FloatArray {
        val magnitude = FloatArray(data.size / 2)
        for (i in magnitude.indices) {
            val re = data[2 * i]
            val im = data[2 * i + 1]
            magnitude[i] = sqrt(re * re + im * im)
        }
        return magnitude
    }
}
