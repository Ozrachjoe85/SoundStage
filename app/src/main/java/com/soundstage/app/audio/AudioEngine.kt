package com.soundstage.app.audio

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class AudioEngine(context: Context) {

    private val player = ExoPlayer.Builder(context).build()

    fun play(path: String) {
        val mediaItem = MediaItem.fromUri(path)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() = player.pause()

    fun release() = player.release()
}
