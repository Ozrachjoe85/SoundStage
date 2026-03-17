package com.autoeq.studio

import android.media.AudioManager
import android.media.audiofx.Equalizer
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.autoeq.studio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var visualizer: Visualizer? = null
    private var equalizer: Equalizer? = null
    private val numBands = 32

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAudioEffects()
        setupRecyclerView()
        setupEqSliders()
    }

    private fun setupAudioEffects() {
        val audioSessionId = (getSystemService(AudioManager::class.java) as AudioManager).mode

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

        equalizer = Equalizer(0, audioSessionId).apply {
            enabled = true
        }
    }

    private fun setupRecyclerView() {
        val items = listOf("Track 1", "Track 2", "Track 3", "Track 4")
        val adapter = TrackAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupEqSliders() {
        val minLevel = equalizer?.bandLevelRange?.get(0) ?: -1500
        val maxLevel = equalizer?.bandLevelRange?.get(1) ?: 1500
        binding.eqLayout.removeAllViews()

        for (i in 0 until numBands) {
            val band = equalizer?.getBand(i.toShort()) ?: 0
            val seekBar = SeekBar(this).apply {
                max = maxLevel - minLevel
                progress = (equalizer?.getBandLevel(band) ?: 0) - minLevel
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?, progress: Int, fromUser: Boolean
                    ) {
                        equalizer?.setBandLevel(band, (progress + minLevel).toShort())
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
            binding.eqLayout.addView(seekBar)
        }
    }

    override fun onDestroy() {
        visualizer?.release()
        equalizer?.release()
        super.onDestroy()
    }
}
