package com.soniclab.app.di

import android.content.Context
import com.soniclab.app.audio.IntelligentEQManager
import com.soniclab.app.playback.MusicScanner
import com.soniclab.app.playback.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {
    
    @Provides
    @Singleton
    fun providePlayerManager(
        @ApplicationContext context: Context,
        eqManager: IntelligentEQManager
    ): PlayerManager {
        return PlayerManager(context, eqManager)
    }
    
    @Provides
    @Singleton
    fun provideMusicScanner(
        @ApplicationContext context: Context
    ): MusicScanner {
        return MusicScanner(context)
    }
}
