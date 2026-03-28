package com.soniclab.app.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Song Profile Entity - Stores learned characteristics
 */
@Entity(tableName = "song_profiles")
data class SongProfileEntity(
    @PrimaryKey val trackId: Long,
    val playCount: Int = 0,
    val lastPlayed: Long = System.currentTimeMillis(),
    
    // Frequency profile
    val bass: Float = 0f,
    val lowMid: Float = 0f,
    val mid: Float = 0f,
    val highMid: Float = 0f,
    val treble: Float = 0f,
    
    // Audio characteristics
    val dynamicRange: Float = 0f,
    val peakAmplitude: Float = 0f,
    val tempo: Float = 120f,
    val energy: Float = 0.5f,
    
    // Learned EQ settings
    val eq60Hz: Float = 0f,
    val eq230Hz: Float = 0f,
    val eq910Hz: Float = 0f,
    val eq3_6kHz: Float = 0f,
    val eq14kHz: Float = 0f,
    val masterGain: Float = 1.0f,
    
    // Learning status
    val isLearned: Boolean = false,  // True after first complete playthrough
    val confidence: Float = 0f       // 0-1, increases with each play
)

@Dao
interface SongProfileDao {
    
    @Query("SELECT * FROM song_profiles WHERE trackId = :trackId")
    suspend fun getProfile(trackId: Long): SongProfileEntity?
    
    @Query("SELECT * FROM song_profiles WHERE trackId = :trackId")
    fun getProfileFlow(trackId: Long): Flow<SongProfileEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: SongProfileEntity)
    
    @Update
    suspend fun updateProfile(profile: SongProfileEntity)
    
    @Query("UPDATE song_profiles SET playCount = playCount + 1, lastPlayed = :timestamp WHERE trackId = :trackId")
    suspend fun incrementPlayCount(trackId: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT * FROM song_profiles ORDER BY energy DESC")
    fun getAllProfilesByEnergy(): Flow<List<SongProfileEntity>>
    
    @Query("SELECT * FROM song_profiles ORDER BY lastPlayed DESC LIMIT :limit")
    fun getRecentlyPlayed(limit: Int = 20): Flow<List<SongProfileEntity>>
    
    @Query("DELETE FROM song_profiles WHERE trackId = :trackId")
    suspend fun deleteProfile(trackId: Long)
    
    @Query("SELECT COUNT(*) FROM song_profiles WHERE isLearned = 1")
    fun getLearnedSongCount(): Flow<Int>
}

@Database(
    entities = [SongProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SonicLabDatabase : RoomDatabase() {
    abstract fun songProfileDao(): SongProfileDao
}
