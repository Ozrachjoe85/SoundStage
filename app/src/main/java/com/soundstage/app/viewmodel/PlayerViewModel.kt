package com.soundstage.app.viewmodel

import android.app.Application
import android.media.audiofx.Visualizer
import androidx.lifecycle.AndroidViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.soundstage.app.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    val player: ExoPlayer = ExoPlayer.Builder(application).build()
    
    private val _visualizerData = MutableStateFlow(FloatArray(24) { 0f })
    val visualizerData: StateFlow<FloatArray> = _visualizerData

    private var visualizer: Visualizer? = null

    // Initialize Visualizer when a song starts
    fun setupVisualizer(audioSessionId: Int) {
        visualizer?.release()
        visualizer = Visualizer(audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(v: Visualizer?, wave: ByteArray?, f: Int) {}
                override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, f: Int) {
                    fft?.let {
                        // Map the raw FFT to our 24 UI bars
                        val magnitudes = FloatArray(24)
                        for (i in 0 until 24) {
                            magnitudes[i] = it.getOrNull(i)?.toFloat() ?: 0f
                        }
                        _visualizerData.value = magnitudes
                    }
                }
            }, Visualizer.getMaxCaptureRate() / 2, false, true)
            enabled = true
        }
    }

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun loadAndPlay(song: Song) {
        _currentSong.value = song
        player.setMediaItem(androidx.media3.common.MediaItem.fromUri(song.uri))
        player.prepare()
        player.play()
        _isPlaying.value = true
        setupVisualizer(player.audioSessionId)
    }

    fun togglePlayback() {
        if (player.isPlaying) player.pause() else player.play()
        _isPlaying.value = player.isPlaying
    }

    override fun onCleared() {
        super.onCleared()
        visualizer?.release()
        player.release()
    }
}
