package com.soundstage.app.viewmodel

import android.app.Application
import android.media.audiofx.Visualizer
import androidx.lifecycle.AndroidViewModel
import com.soundstage.app.audio.AudioProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val processor = AudioProcessor()
    
    private val _visualizerData = MutableStateFlow(FloatArray(24) { 0f })
    val visualizerData: StateFlow<FloatArray> = _visualizerData

    private var visualizer: Visualizer? = null

    fun setupVisualizer(audioSessionId: Int) {
        visualizer?.release()
        visualizer = Visualizer(audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(v: Visualizer?, wave: ByteArray?, f: Int) {}
                override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, f: Int) {
                    fft?.let {
                        // Use the now-resolved .process() function
                        val fullMagnitudes = processor.process(it)
                        
                        // Map the full FFT down to our 24 UI bars
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
    }
    
    // ... rest of your existing loadAndPlay and togglePlayback logic
}
