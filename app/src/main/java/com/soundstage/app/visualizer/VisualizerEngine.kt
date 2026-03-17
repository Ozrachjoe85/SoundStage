package com.soundstage.app.visualizer

import android.media.audiofx.Visualizer

class VisualizerEngine(sessionId: Int) {

    private val visualizer = Visualizer(sessionId)

    fun start(onData: (ByteArray) -> Unit) {
        visualizer.setDataCaptureListener(
            object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {
                    data?.let { onData(it) }
                }

                override fun onFftDataCapture(v: Visualizer?, data: ByteArray?, rate: Int) {}
            },
            Visualizer.getMaxCaptureRate() / 2,
            true,
            false
        )
        visualizer.enabled = true
    }
}
