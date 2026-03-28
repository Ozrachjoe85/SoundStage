package com.soniclab.app.di

import android.content.Context
import androidx.room.Room
import com.soniclab.app.audio.AudioAnalysisEngine
import com.soniclab.app.audio.IntelligentEQManager
import com.soniclab.app.database.SonicLabDatabase
import com.soniclab.app.database.SongProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideSonicLabDatabase(
        @ApplicationContext context: Context
    ): SonicLabDatabase {
        return Room.databaseBuilder(
            context,
            SonicLabDatabase::class.java,
            "soniclab_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    @Singleton
    fun provideSongProfileDao(
        database: SonicLabDatabase
    ): SongProfileDao {
        return database.songProfileDao()
    }
    
    @Provides
    @Singleton
    fun provideAudioAnalysisEngine(): AudioAnalysisEngine {
        return AudioAnalysisEngine()
    }
    
    @Provides
    @Singleton
    fun provideIntelligentEQManager(
        songProfileDao: SongProfileDao,
        audioAnalysis: AudioAnalysisEngine
    ): IntelligentEQManager {
        return IntelligentEQManager(songProfileDao, audioAnalysis)
    }
}
