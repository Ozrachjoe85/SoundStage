package com.soundstage.app.audio

import kotlin.math.*

class FFTProcessor {

    fun performFFT(input: FloatArray): FloatArray {
        val n = input.size
        val output = FloatArray(n)

        for (k in 0 until n) {
            var real = 0.0
            var imag = 0.0

            for (t in 0 until n) {
                val angle = 2.0 * PI * t * k / n
                real += input[t] * cos(angle)
                imag -= input[t] * sin(angle)
            }

            output[k] = sqrt(real * real + imag * imag).toFloat()
        }

        return output
    }
}
