package com.soundstage.app.data.models

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val path: String,
    val duration: Long
)
