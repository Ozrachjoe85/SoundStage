package com.soniclab.app.audio

import android.media.audiofx.Visualizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * AudioAnalysisEngine - Analyzes songs and learns their characteristics
 * 
 * This engine processes audio data to understand:
 * - Frequency distribution
 * - Dynamic range
 * - Peak amplitudes
 * - Optimal EQ settings per song
 */
class AudioAnalysisEngine {
    
    data class SongProfile(
        val trackId: Long,
        val playCount: Int = 0,
        val frequencyProfile: FrequencyProfile,
        val dynamicRange: Float,
        val peakAmplitude: Float,
        val optimalEQ: EQSettings,
        val tempo: Float,
        val energy: Float
    )
    
    data class FrequencyProfile(
        val bass: Float,      // 20-250 Hz
        val lowMid: Float,    // 250-500 Hz
        val mid: Float,       // 500-2k Hz
        val highMid: Float,   // 2k-4k Hz
        val treble: Float     // 4k-20k Hz
    )
    
    data class EQSettings(
        val band60Hz: Float,
        val band230Hz: Float,
        val band910Hz: Float,
        val band3_6kHz: Float,
        val band14kHz: Float,
        val masterGain: Float  // Overall volume adjustment to prevent clipping
    )
    
    /**
     * Analyze audio data from visualizer
     */
    suspend fun analyzeAudioFrame(fft: ByteArray): FrequencyProfile = withContext(Dispatchers.Default) {
        val magnitudes = fftToMagnitudes(fft)
        
        FrequencyProfile(
            bass = calculateBandEnergy(magnitudes, 0, 10),
            lowMid = calculateBandEnergy(magnitudes, 10, 20),
            mid = calculateBandEnergy(magnitudes, 20, 40),
            highMid = calculateBandEnergy(magnitudes, 40, 60),
            treble = calculateBandEnergy(magnitudes, 60, magnitudes.size)
        )
    }
    
    private fun fftToMagnitudes(fft: ByteArray): FloatArray {
        val magnitudes = FloatArray(fft.size / 2)
        for (i in magnitudes.indices) {
            val real = fft[i * 2].toFloat()
            val imag = fft[i * 2 + 1].toFloat()
            magnitudes[i] = sqrt(real * real + imag * imag)
        }
        return magnitudes
    }
    
    private fun calculateBandEnergy(magnitudes: FloatArray, start: Int, end: Int): Float {
        val endIdx = minOf(end, magnitudes.size)
        var sum = 0f
        for (i in start until endIdx) {
            sum += magnitudes[i]
        }
        return sum / (endIdx - start)
    }
    
    /**
     * Calculate optimal EQ based on frequency profile
     * First play: Returns flat (learning)
     * Subsequent plays: Returns optimized settings
     */
    fun calculateOptimalEQ(profile: FrequencyProfile, playCount: Int): EQSettings {
        // First play - return flat for learning
        if (playCount == 0) {
            return EQSettings(0f, 0f, 0f, 0f, 0f, 1.0f)
        }
        
        // Analyze frequency balance
        val total = profile.bass + profile.lowMid + profile.mid + profile.highMid + profile.treble
        val avgEnergy = total / 5f
        
        // Calculate compensations (boost weak areas, reduce strong areas)
        val bassCompensation = calculateCompensation(profile.bass, avgEnergy)
        val lowMidCompensation = calculateCompensation(profile.lowMid, avgEnergy)
        val midCompensation = calculateCompensation(profile.mid, avgEnergy)
        val highMidCompensation = calculateCompensation(profile.highMid, avgEnergy)
        val trebleCompensation = calculateCompensation(profile.treble, avgEnergy)
        
        // Calculate master gain to prevent clipping
        val maxBoost = maxOf(
            bassCompensation, lowMidCompensation, midCompensation,
            highMidCompensation, trebleCompensation
        )
        val masterGain = if (maxBoost > 0) 1.0f / (1.0f + maxBoost * 0.1f) else 1.0f
        
        return EQSettings(
            band60Hz = bassCompensation,
            band230Hz = lowMidCompensation,
            band910Hz = midCompensation,
            band3_6kHz = highMidCompensation,
            band14kHz = trebleCompensation,
            masterGain = masterGain
        )
    }
    
    private fun calculateCompensation(bandEnergy: Float, avgEnergy: Float): Float {
        val ratio = bandEnergy / avgEnergy
        return when {
            ratio < 0.7f -> 0.3f   // Boost weak frequencies
            ratio > 1.3f -> -0.2f  // Reduce overpowering frequencies
            else -> 0f             // Balanced
        }
    }
    
    /**
     * Calculate dynamic range in dB
     */
    fun calculateDynamicRange(magnitudes: FloatArray): Float {
        val max = magnitudes.maxOrNull() ?: 1f
        val min = magnitudes.filter { it > 0 }.minOrNull() ?: 1f
        return 20 * log10(max / min)
    }
    
    /**
     * Estimate tempo from frequency data
     */
    fun estimateTempo(profiles: List<FrequencyProfile>): Float {
        // Simple tempo estimation based on bass pattern repetition
        if (profiles.size < 20) return 120f
        
        val bassPeaks = profiles.map { it.bass }
        var peakCount = 0
        var lastPeak = false
        
        bassPeaks.forEach { bass ->
            val isPeak = bass > bassPeaks.average() * 1.2
            if (isPeak && !lastPeak) peakCount++
            lastPeak = isPeak
        }
        
        // Estimate BPM based on peak frequency
        val duration = profiles.size * 0.1f // Assuming 100ms per profile
        return (peakCount / duration) * 60f
    }
    
    /**
     * Calculate overall energy level
     */
    fun calculateEnergy(profile: FrequencyProfile): Float {
        return (profile.bass + profile.lowMid + profile.mid + 
                profile.highMid + profile.treble) / 5f
    }
}
