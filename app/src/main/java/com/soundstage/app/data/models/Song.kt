package com.soundstage.app.data.models

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: Long,
    val path: String = ""
)
