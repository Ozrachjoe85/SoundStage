package com.autoeq.studio

import android.media.AudioManager
import android.media.audiofx.Visualizer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.autoeq.studio.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var visualizer: Visualizer? = null
    private val audioSessionId: Int
        get() = (applicationContext.getSystemService(AudioManager::class.java) as AudioManager).mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupVisualizer()
    }

    private fun setupRecyclerView() {
        val items = listOf(
            "Track 1", "Track 2", "Track 3", "Track 4"
        )
        val adapter = TrackAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupVisualizer() {
        visualizer = Visualizer(audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer?, bytes: ByteArray?, samplingRate: Int
                ) {
                    bytes?.let { binding.visualizerView.updateVisualizer(it) }
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer?, bytes: ByteArray?, samplingRate: Int
                ) { }
            }, Visualizer.getMaxCaptureRate() / 2, true, false)
            enabled = true
        }
    }

    override fun onDestroy() {
        visualizer?.release()
        super.onDestroy()
    }
}
