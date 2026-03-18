package com.soundstage.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize the Media3 Player
    val player: ExoPlayer = ExoPlayer.Builder(application).build()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun togglePlayback() {
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
        } else {
            player.play()
            _isPlaying.value = true
        }
    }

    fun onEqUpdate(band: Int, value: Float) {
        // Future implementation for AudioProcessor EQ logic
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
