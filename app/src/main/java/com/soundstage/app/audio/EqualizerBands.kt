package com.soundstage.app.audio

import kotlin.math.pow

data class EQBand(
    val frequency: Float,
    val gain: Float,
    val isBypassed: Boolean = false
)

object EqualizerBands {
    val defaultBands = listOf(
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
