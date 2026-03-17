package com.soundstage.app.viewmodel

import androidx.lifecycle.ViewModel
import com.soundstage.app.audio.AudioEngine
import com.soundstage.app.audio.EQBand
import com.soundstage.app.data.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(private val audioEngine: AudioEngine) : ViewModel() {

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _eqBands = MutableStateFlow<List<EQBand>>(emptyList())
    val eqBands = _eqBands.asStateFlow()

    init {
        audioEngine.onEqUpdate = { bands ->
            _eqBands.value = bands
        }
    }

    fun playTrack(track: Track) {
        _currentTrack.value = track
        audioEngine.play(track.path)
        _isPlaying.value = true
    }

    fun togglePlayback() {
        if (_isPlaying.value) {
            audioEngine.pause()
        } else {
            _currentTrack.value?.let { audioEngine.play(it.path) }
        }
        _isPlaying.value = !_isPlaying.value
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.release()
    }
}

