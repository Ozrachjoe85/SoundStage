package com.soundstage.app.audio

class AudioProcessor {

    private val autoEq = AutoEqEngine()

    fun process(input: FloatArray): Pair<FloatArray, List<EQBand>> {

        val bands = autoEq.process(input)

        val output = FloatArray(input.size)

        for (i in input.indices) {
            var sample = input[i]

            // Apply EQ influence (very basic for now)
            val bandIndex = (i % bands.size)
            val gain = bands[bandIndex].gain

            sample *= (1f + gain / 10f)

            output[i] = sample.coerceIn(-1f, 1f)
        }

        return Pair(output, bands)
    }
}
