/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.di.modules

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val playerModule = module {
    factory<Player> {
        val renderersFactory = DefaultRenderersFactory(androidContext()).apply {
            setExtensionRendererMode(EXTENSION_RENDERER_MODE_PREFER)
        }

        val trackSelector = DefaultTrackSelector(androidContext()).apply {
            parameters = buildUponParameters()
                .setAllowAudioMixedDecoderSupportAdaptiveness(true)
                .setAllowAudioMixedMimeTypeAdaptiveness(true)
                .build()
        }

        val defaultDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

        val source = HlsMediaSource.Factory(defaultDataSourceFactory)
            .setAllowChunklessPreparation(true)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        ExoPlayer.Builder(androidContext())
            .setAudioAttributes(audioAttributes, true)
            .setTrackSelector(trackSelector)
            .setRenderersFactory(renderersFactory)
            .setMediaSourceFactory(source)
            .build()
    }
}