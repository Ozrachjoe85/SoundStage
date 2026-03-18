package com.soundstage.app.audio

data class EQBand(
    val frequency: Float,
    var gain: Float, // Changed to var so we can adjust it
    val isBypassed: Boolean = false
)

object EqualizerBands {
    fun createBands(): List<EQBand> {
        return listOf(
            EQBand(32f, 0f),
            EQBand(64f, 0f),
            EQBand(125f, 0f),
            EQBand(250f, 0f),
            EQBand(500f, 0f),
            EQBand(1000f, 0f),
            EQBand(2000f, 0f),
            EQBand(4000f, 0f),
            EQBand(8000f, 0f),
            EQBand(16000f, 0f)
        )
    }
}
