package com.soundstage.app.visualizer

import android.media.audiofx.Visualizer
import android.util.Log

class VisualizerEngine {
    private var visualizer: Visualizer? = null

    fun startWaveform(audioSessionId: Int, onData: (FloatArray) -> Unit) {
        try {
            // Initialize with the current audio session ID from ExoPlayer
            visualizer = Visualizer(audioSessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]
                
                setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {
                            data?.let {
                                // Convert signed bytes to normalized floats [-1.0, 1.0]
                                val floatData = FloatArray(it.size) { i ->
                                    (it[i].toInt() and 0xFF - 128) / 128f
                                }
                                onData(floatData)
                            }
                        }

                        override fun onFftDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {
                            // This is where the "Digital Segmented Bars" data comes from
                        }
                    },
                    Visualizer.getMaxCaptureRate() / 2,
                    true,
                    false
                )
                enabled = true
            }
        } catch (e: Exception) {
            Log.e("VisualizerEngine", "Failed to initialize visualizer", e)
        }
    }

    fun stop() {
        visualizer?.apply {
            enabled = false
            release()
        }
        visualizer = null
    }
}
