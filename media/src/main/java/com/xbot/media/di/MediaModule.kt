package com.xbot.media.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    @Provides
    @Singleton
    fun providePlayer(
        @ApplicationContext context: Context,
    ): Player {
        return ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}

/*
 * Created by AnyGogin31 on 10.11.2024
 */

val mediaModule = module {
    single {
        ExoPlayer.Builder(androidContext())
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}
