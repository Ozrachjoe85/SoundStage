package com.soundstage.app.audio

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.*
import kotlin.random.Random

class AudioEngine(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private val processor = AudioProcessor()

    private var processingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // 🔥 UI listener (Equalizer screen will use this)
    var onEqUpdate: ((List<EQBand>) -> Unit)? = null

    // 🎵 PLAY TRACK
    fun play(path: String) {
        val mediaItem = MediaItem.fromUri(path)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        startProcessingLoop()
    }

    // ⏸️ PAUSE
    fun pause() {
        player.pause()
        stopProcessingLoop()
    }

    // ⏹️ STOP
    fun stop() {
        player.stop()
        stopProcessingLoop()
    }

    // 🔄 RELEASE
    fun release() {
        stopProcessingLoop()
        player.release()
        scope.cancel()
    }

    // 🎧 PROCESSING LOOP (SIMULATED REAL-TIME FOR NOW)
    private fun startProcessingLoop() {
        stopProcessingLoop()

        processingJob = scope.launch {
            while (isActive && player.isPlaying) {

                // ⚠️ TEMP: Fake audio buffer (until real PCM tap)
                val fakeAudio = FloatArray(1024) {
                    Random.nextFloat() * 2f - 1f
                }

                val (_, bands) = processor.process(fakeAudio)

                // Send EQ updates to UI
                withContext(Dispatchers.Main) {
                    onEqUpdate?.invoke(bands)
                }

                delay(100) // ~10 updates/sec (smooth enough, not heavy)
            }
        }
    }

    private fun stopProcessingLoop() {
        processingJob?.cancel()
        processingJob = null
    }
}
