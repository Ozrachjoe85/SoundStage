package com.soundstage.app.viewmodel

import android.app.Application
import android.media.audiofx.Visualizer
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.soundstage.app.audio.AudioProcessor
import com.soundstage.app.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(UnstableApi::class) // This line satisfies the Lint error
class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    val player: ExoPlayer = ExoPlayer.Builder(application).build()
    private val processor = AudioProcessor()
    
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _visualizerData = MutableStateFlow(FloatArray(24) { 0f })
    val visualizerData: StateFlow<FloatArray> = _visualizerData

    private var visualizer: Visualizer? = null

    fun loadAndPlay(song: Song) {
        _currentSong.value = song
        val mediaItem = MediaItem.fromUri(song.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        _isPlaying.value = true
        setupVisualizer(player.audioSessionId)
    }

    fun togglePlayback() {
        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
        } else {
            player.play()
            _isPlaying.value = true
        }
    }

    private fun setupVisualizer(audioSessionId: Int) {
        visualizer?.release()
        try {
            visualizer = Visualizer(audioSessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(v: Visualizer?, wave: ByteArray?, f: Int) {}
                    override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, f: Int) {
                        fft?.let {
                            val fullMagnitudes = processor.process(it)
                            val uiBars = FloatArray(24)
                            for (i in 0 until 24) {
                                uiBars[i] = fullMagnitudes.getOrElse(i) { 0f }
                            }
                            _visualizerData.value = uiBars
                        }
                    }
                }, Visualizer.getMaxCaptureRate() / 2, false, true)
                enabled = true
            }
        } catch (e: Exception) {
            // Silently fail if visualizer cannot be attached
        }
    }

    override fun onCleared() {
        super.onCleared()
        visualizer?.release()
        player.release()
    }
}
