package com.soniclab.app.audio

import android.media.audiofx.Equalizer
import android.media.audiofx.Visualizer
import android.util.Log
import com.soniclab.app.database.SongProfileDao
import com.soniclab.app.database.SongProfileEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class EQMode {
    AUTO,    // Intelligent auto-EQ based on song learning
    MANUAL   // User-controlled EQ
}

@Singleton
class IntelligentEQManager @Inject constructor(
    private val songProfileDao: SongProfileDao,
    private val audioAnalysis: AudioAnalysisEngine
) {
    private var equalizer: Equalizer? = null
    private var visualizer: Visualizer? = null
    private var analysisJob: Job? = null
    
    private val _mode = MutableStateFlow(EQMode.AUTO)
    val mode: StateFlow<EQMode> = _mode.asStateFlow()
    
    private val _isEnabled = MutableStateFlow(true)
    val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()
    
    private val _currentProfile = MutableStateFlow<SongProfileEntity?>(null)
    val currentProfile: StateFlow<SongProfileEntity?> = _currentProfile.asStateFlow()
    
    private var currentTrackId: Long? = null
    private val capturedProfiles = mutableListOf<AudioAnalysisEngine.FrequencyProfile>()
    
    /**
     * Initialize EQ and visualizer for audio session
     */
    fun initialize(audioSessionId: Int) {
        release()
        
        try {
            equalizer = Equalizer(0, audioSessionId).apply {
                enabled = true
            }
            
            visualizer = Visualizer(audioSessionId).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]
                setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer?,
                            waveform: ByteArray?,
                            samplingRate: Int
                        ) {
                            // Not used
                        }
                        
                        override fun onFftDataCapture(
                            visualizer: Visualizer?,
                            fft: ByteArray?,
                            samplingRate: Int
                        ) {
                            fft?.let { processAudioData(it) }
                        }
                    },
                    Visualizer.getMaxCaptureRate() / 2,
                    false,
                    true
                )
                enabled = true
            }
        } catch (e: Exception) {
            Log.e("IntelligentEQManager", "Failed to initialize: ${e.message}")
        }
    }
    
    /**
     * Start learning/applying EQ for a track
     */
    suspend fun startTrackAnalysis(trackId: Long) {
        currentTrackId = trackId
        capturedProfiles.clear()
        
        // Load existing profile
        val profile = songProfileDao.getProfile(trackId)
        _currentProfile.value = profile
        
        if (_mode.value == EQMode.AUTO) {
            if (profile?.isLearned == true) {
                // Apply learned EQ immediately
                applyEQSettings(profile)
            } else {
                // First play - flat EQ while learning
                applyFlatEQ()
            }
        }
        
        // Increment play count
        if (profile != null) {
            songProfileDao.incrementPlayCount(trackId)
        }
    }
    
    /**
     * Process incoming audio data
     */
    private fun processAudioData(fft: ByteArray) {
        if (_mode.value != EQMode.AUTO || currentTrackId == null) return
        
        analysisJob?.cancel()
        analysisJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                val frequencyProfile = audioAnalysis.analyzeAudioFrame(fft)
                capturedProfiles.add(frequencyProfile)
                
                // Update profile every 100 samples
                if (capturedProfiles.size >= 100) {
                    updateSongProfile()
                }
            } catch (e: Exception) {
                Log.e("IntelligentEQManager", "Analysis error: ${e.message}")
            }
        }
    }
    
    /**
     * Update song profile in database
     */
    private suspend fun updateSongProfile() {
        val trackId = currentTrackId ?: return
        if (capturedProfiles.isEmpty()) return
        
        // Calculate average frequency profile
        val avgProfile = AudioAnalysisEngine.FrequencyProfile(
            bass = capturedProfiles.map { it.bass }.average().toFloat(),
            lowMid = capturedProfiles.map { it.lowMid }.average().toFloat(),
            mid = capturedProfiles.map { it.mid }.average().toFloat(),
            highMid = capturedProfiles.map { it.highMid }.average().toFloat(),
            treble = capturedProfiles.map { it.treble }.average().toFloat()
        )
        
        val existingProfile = songProfileDao.getProfile(trackId)
        val playCount = (existingProfile?.playCount ?: 0) + 1
        
        // Calculate optimal EQ
        val optimalEQ = audioAnalysis.calculateOptimalEQ(avgProfile, playCount - 1)
        
        // Calculate tempo and energy
        val tempo = audioAnalysis.estimateTempo(capturedProfiles)
        val energy = audioAnalysis.calculateEnergy(avgProfile)
        
        val updatedProfile = SongProfileEntity(
            trackId = trackId,
            playCount = playCount,
            bass = avgProfile.bass,
            lowMid = avgProfile.lowMid,
            mid = avgProfile.mid,
            highMid = avgProfile.highMid,
            treble = avgProfile.treble,
            dynamicRange = 0f,
            peakAmplitude = 0f,
            tempo = tempo,
            energy = energy,
            eq60Hz = optimalEQ.band60Hz,
            eq230Hz = optimalEQ.band230Hz,
            eq910Hz = optimalEQ.band3_6kHz,
            eq3_6kHz = optimalEQ.band3_6kHz,
            eq14kHz = optimalEQ.band14kHz,
            masterGain = optimalEQ.masterGain,
            isLearned = playCount > 0,
            confidence = minOf(playCount / 3f, 1f)
        )
        
        songProfileDao.insertProfile(updatedProfile)
        _currentProfile.value = updatedProfile
        
        // Apply new EQ if this isn't first play
        if (playCount > 1) {
            applyEQSettings(updatedProfile)
        }
        
        capturedProfiles.clear()
    }
    
    /**
     * Apply EQ settings from profile
     */
    private fun applyEQSettings(profile: SongProfileEntity) {
        equalizer?.let { eq ->
            val numBands = eq.numberOfBands.toInt()
            if (numBands >= 5) {
                setBandLevel(eq, 0, profile.eq60Hz)
                setBandLevel(eq, 1, profile.eq230Hz)
                setBandLevel(eq, 2, profile.eq910Hz)
                setBandLevel(eq, 3, profile.eq3_6kHz)
                setBandLevel(eq, 4, profile.eq14kHz)
            }
        }
    }
    
    /**
     * Apply flat EQ (learning mode)
     */
    private fun applyFlatEQ() {
        equalizer?.let { eq ->
            for (i in 0 until eq.numberOfBands.toInt()) {
                eq.setBandLevel(i.toShort(), 0)
            }
        }
    }
    
    /**
     * Set band level with normalization
     */
    private fun setBandLevel(eq: Equalizer, bandIndex: Int, level: Float) {
        val minLevel = eq.bandLevelRange[0]
        val maxLevel = eq.bandLevelRange[1]
        val actualLevel = (level * (maxLevel - minLevel) + minLevel).toInt().toShort()
        eq.setBandLevel(bandIndex.toShort(), actualLevel)
    }
    
    /**
     * Switch between auto and manual modes
     */
    fun setMode(newMode: EQMode) {
        _mode.value = newMode
        if (newMode == EQMode.AUTO) {
            _currentProfile.value?.let { applyEQSettings(it) }
        }
    }
    
    /**
     * Enable/disable EQ
     */
    fun setEnabled(enabled: Boolean) {
        _isEnabled.value = enabled
        equalizer?.enabled = enabled
    }
    
    /**
     * Manual EQ adjustment
     */
    fun setManualBandLevel(bandIndex: Int, level: Float) {
        if (_mode.value == EQMode.MANUAL) {
            equalizer?.let { setBandLevel(it, bandIndex, level) }
        }
    }
    
    /**
     * Get visualizer for UI
     */
    fun getVisualizer(): Visualizer? = visualizer
    
    /**
     * Release resources
     */
    fun release() {
        analysisJob?.cancel()
        visualizer?.release()
        equalizer?.release()
        visualizer = null
        equalizer = null
    }
}
