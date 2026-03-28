package com.soniclab.app.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.soniclab.app.audio.IntelligentEQManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class Track(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val albumArtUri: String?
)

enum class RepeatMode {
    OFF, ONE, ALL
}

@Singleton
class PlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eqManager: IntelligentEQManager
) {
    private var player: ExoPlayer? = null
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()
    
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()
    
    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()
    
    private var originalQueue: List<Track> = emptyList()
    
    fun initialize() {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        _isPlaying.value = playing
                    }
                    
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            handleTrackEnded()
                        } else if (state == Player.STATE_READY && playing) {
                            // Initialize EQ when playback starts
                            initializeEQForCurrentTrack()
                        }
                    }
                })
            }
        }
    }
    
    private fun initializeEQForCurrentTrack() {
        player?.let { p ->
            // Initialize EQ with audio session
            eqManager.initialize(p.audioSessionId)
            
            // Start analysis for current track
            _currentTrack.value?.let { track ->
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                    eqManager.startTrackAnalysis(track.id)
                }
            }
        }
    }
    
    fun setQueueAndPlay(tracks: List<Track>, startIndex: Int = 0) {
        originalQueue = tracks
        _queue.value = tracks
        _currentIndex.value = startIndex
        
        if (_shuffleEnabled.value) {
            applyShuffleToQueue()
        }
        
        prepareQueue()
        player?.seekToDefaultPosition(startIndex)
        _currentTrack.value = tracks.getOrNull(startIndex)
    }
    
    private fun prepareQueue() {
        val mediaItems = _queue.value.map { track ->
            MediaItem.fromUri(Uri.parse(track.uri))
        }
        player?.setMediaItems(mediaItems)
        player?.prepare()
    }
    
    private fun handleTrackEnded() {
        when (_repeatMode.value) {
            RepeatMode.ONE -> {
                player?.seekTo(0)
                player?.play()
            }
            RepeatMode.ALL -> playNext()
            RepeatMode.OFF -> {
                val nextIndex = _currentIndex.value + 1
                if (nextIndex < _queue.value.size) {
                    playNext()
                } else {
                    pause()
                }
            }
        }
    }
    
    fun togglePlayPause() {
        if (_isPlaying.value) pause() else play()
    }
    
    fun play() {
        player?.play()
    }
    
    fun pause() {
        player?.pause()
    }
    
    fun playNext() {
        val nextIndex = (_currentIndex.value + 1) % _queue.value.size
        _currentIndex.value = nextIndex
        _currentTrack.value = _queue.value.getOrNull(nextIndex)
        player?.seekToDefaultPosition(nextIndex)
        player?.play()
    }
    
    fun playPrevious() {
        val currentPos = getCurrentPosition()
        if (currentPos > 3000) {
            player?.seekTo(0)
        } else {
            val prevIndex = if (_currentIndex.value > 0) {
                _currentIndex.value - 1
            } else {
                _queue.value.size - 1
            }
            _currentIndex.value = prevIndex
            _currentTrack.value = _queue.value.getOrNull(prevIndex)
            player?.seekToDefaultPosition(prevIndex)
        }
        player?.play()
    }
    
    fun seekTo(position: Long) {
        player?.seekTo(position)
    }
    
    fun getCurrentPosition(): Long = player?.currentPosition ?: 0L
    
    fun getDuration(): Long = player?.duration?.takeIf { it > 0 } ?: 0L
    
    fun getPlayer(): Player = player!!
    
    fun getAudioSessionId(): Int = player?.audioSessionId ?: 0
    
    fun toggleShuffle() {
        _shuffleEnabled.value = !_shuffleEnabled.value
        if (_shuffleEnabled.value) {
            applyShuffleToQueue()
        } else {
            _queue.value = originalQueue
            val currentTrack = _currentTrack.value
            _currentIndex.value = originalQueue.indexOf(currentTrack)
        }
        prepareQueue()
    }
    
    private fun applyShuffleToQueue() {
        val currentTrack = _currentTrack.value
        val mutableQueue = originalQueue.toMutableList()
        
        currentTrack?.let {
            mutableQueue.remove(it)
            mutableQueue.shuffle()
            mutableQueue.add(0, it)
        } ?: mutableQueue.shuffle()
        
        _queue.value = mutableQueue
        _currentIndex.value = 0
    }
    
    fun cycleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }
    
    fun release() {
        eqManager.release()
        player?.release()
        player = null
    }
}
