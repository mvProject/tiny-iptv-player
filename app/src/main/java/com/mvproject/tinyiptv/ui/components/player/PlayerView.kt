/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 05.10.23, 18:32
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mvproject.tinyiptv.data.enums.player.ViewSettingsRequest
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.modifiers.adaptiveLayout
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerTapGesturesState
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptv.ui.screens.player.VideoPlayerState
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerState: VideoPlayerState,
    programs: List<EpgProgram>,
    onViewSettingsAction: (action: ViewSettingsRequest) -> Unit = {},
    channelName: String,
    onPlaybackAction: (PlaybackActions) -> Unit = {}
) {

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

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
                    aspectRatio = playerState.videoSize.value,
                    resizeMode = playerState.videoResizeMode.value
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

        AnimatedVisibility(
            visible = playerState.isControlUiVisible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            PlayerChannelView(
                modifier = Modifier.fillMaxSize(),
                channelName = channelName,
                epgPrograms = programs,
                playerState = playerState,
                onPlaybackAction = onPlaybackAction
            )
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    onPlaybackAction(PlaybackActions.OnPlayerUiToggle)
                }

                Lifecycle.Event.ON_STOP -> playerState.player.pause()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}