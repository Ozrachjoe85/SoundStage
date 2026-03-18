package com.soundstage.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.soundstage.app.data.MusicRepository
import com.soundstage.app.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository(application.contentResolver)
    private val _uiState = MutableStateFlow<List<Song>>(emptyList())
    val uiState: StateFlow<List<Song>> = _uiState

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    // Initialize Media3 ExoPlayer
    val player: ExoPlayer by lazy {
        ExoPlayer.Builder(application).build()
    }

    fun loadMusic() {
        viewModelScope.launch {
            val songs = repository.fetchSongs()
            _uiState.value = songs
        }
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        val mediaItem = MediaItem.fromUri(song.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
