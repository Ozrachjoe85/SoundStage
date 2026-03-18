package com.soundstage.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.soundstage.app.audio.CoreAudioState

class CoreViewModel : ViewModel() {

    private val _state = MutableStateFlow(CoreAudioState())
    val state: StateFlow<CoreAudioState> = _state

    fun setBass(value: Float) {
        _state.update { it.copy(bass = value) }
    }

    fun setMid(value: Float) {
        _state.update { it.copy(mid = value) }
    }

    fun setTreble(value: Float) {
        _state.update { it.copy(treble = value) }
    }

    fun setWidth(value: Float) {
        _state.update { it.copy(width = value) }
    }
}
