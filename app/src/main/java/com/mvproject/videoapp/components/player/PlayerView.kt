/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.player

import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.Player
import com.mvproject.videoapp.components.modifiers.adaptiveLayout
import com.mvproject.videoapp.components.modifiers.defaultPlayerTapGestures
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.presentation.player.VideoViewViewModel
import io.github.aakira.napier.Napier

@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier,
    playerState: VideoViewViewModel.ControlUIState,
    player: Player,
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {},
    controller: @Composable () -> Unit,
) {
    SideEffect {
        Napier.w("testing VideoPlayerView SideEffect")
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = modifier
            .defaultPlayerTapGestures(onPlayerUICommand)
    ) {
        AndroidView(
            modifier = Modifier
                .adaptiveLayout(
                    aspectRatio = playerState.videoSizeRatio,
                    resizeMode = playerState.videoResizeMode
                ),
            factory = { context ->
                SurfaceView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    keepScreenOn = true
                }.also { view ->
                    player.setVideoSurfaceView(view)
                    player
                        .trackSelectionParameters
                        .buildUpon()
                        .setMaxVideoSizeSd()
                        .build()
                }
            }
        )

        controller()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    onPlayerUICommand(PlayerUICommands.CONTROL_UI_ON)
                }

                Lifecycle.Event.ON_STOP -> player.pause()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}