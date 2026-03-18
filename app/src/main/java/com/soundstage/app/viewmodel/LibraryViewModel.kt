package com.soundstage.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.soundstage.app.data.MusicRepository
import com.soundstage.app.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MusicRepository(application.contentResolver)
    
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    fun loadLibrary() {
        viewModelScope.launch {
            _songs.value = repository.fetchSongs()
        }
    }
}
