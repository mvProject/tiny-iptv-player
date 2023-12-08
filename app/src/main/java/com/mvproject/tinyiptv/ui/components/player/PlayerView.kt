/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:15
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import com.mvproject.tinyiptv.ui.components.ConnectionState
import com.mvproject.tinyiptv.ui.components.modifiers.adaptiveLayout
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerTapGesturesState
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptv.ui.components.networkConnectionState
import com.mvproject.tinyiptv.ui.components.rememberSystemUIController
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.ui.screens.player.state.rememberVideoPlayerState
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    videoViewState: VideoViewState,
    onPlaybackAction: (PlaybackActions) -> Unit = {},
    onPlaybackStateAction: (PlaybackStateActions) -> Unit = {},
    controls: @Composable () -> Unit
) {

    val connection by networkConnectionState()
    when (connection) {
        ConnectionState.Available -> Napier.w("testing connectivity is available")
        ConnectionState.Unavailable -> Napier.w("testing connectivity is unavailable")
    }

    val playerState = rememberVideoPlayerState(
        onPlaybackStateAction = onPlaybackStateAction
    )
    val systemUIController = rememberSystemUIController()

    LaunchedEffect(videoViewState.isFullscreen) {
        systemUIController.isSystemBarsVisible = !videoViewState.isFullscreen
    }


    LaunchedEffect(videoViewState.isRestartRequired) {
        if (videoViewState.isRestartRequired) {
            playerState.restartPlayer()
            onPlaybackAction(PlaybackActions.OnRestarted)
        }
    }

    LaunchedEffect(videoViewState.isFullscreen) {
        systemUIController.isSystemBarsVisible = !videoViewState.isFullscreen
    }

    LaunchedEffect(videoViewState.currentVolume) {
        playerState.setVolume(videoViewState.currentVolume)
    }

    LaunchedEffect(videoViewState.mediaPosition) {
        if (videoViewState.mediaPosition > AppConstants.INT_NO_VALUE) {
            playerState.setPlayerChannel(
                channelName = videoViewState.currentChannel.channelName,
                channelUrl = videoViewState.currentChannel.channelUrl
            )
        }
    }

    LaunchedEffect(videoViewState.isPlaying) {
        if (playerState.player.isPlaying != videoViewState.isPlaying) {
            playerState.setPlayingState(videoViewState.isPlaying)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .defaultPlayerHorizontalGestures(onAction = onPlaybackAction)
            .defaultPlayerVerticalGestures(onAction = onPlaybackAction)
            .defaultPlayerTapGesturesState(onAction = onPlaybackAction),
    ) {
        AndroidView(
            modifier = Modifier
                .adaptiveLayout(
                    aspectRatio = videoViewState.videoRatio,
                    resizeMode = videoViewState.videoResizeMode
                ),
            factory = { context ->
                SurfaceView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    keepScreenOn = true
                }.also { view ->
                    playerState.player.setVideoSurfaceView(view)
                }
            }
        )

        controls()
    }

    LifecycleStartEffect(playerState) {
        onStopOrDispose {
            playerState.player.stop()
            systemUIController.isSystemBarsVisible = true
        }
    }
}