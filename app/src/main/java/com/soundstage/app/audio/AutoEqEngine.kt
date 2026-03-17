package com.soundstage.app.audio

class AutoEqEngine {

    private val fft = FFTProcessor()
    private var bands = EqualizerBands.createBands(32)

    fun process(audioData: FloatArray): List<EQBand> {
        val spectrum = fft.performFFT(audioData)

        val bandSize = spectrum.size / bands.size

        bands.forEachIndexed { index, band ->
            val start = index * bandSize
            val end = start + bandSize

            var sum = 0f
            for (i in start until end) {
                sum += spectrum.getOrElse(i) { 0f }
            }

            val avg = sum / bandSize

            // Normalize + invert (boost weak areas)
            band.gain = normalize(avg)
        }

        return bands
    }

    private fun normalize(value: Float): Float {
        return ((value / 1000f).coerceIn(0f, 1f) - 0.5f) * 10f
    }
}
