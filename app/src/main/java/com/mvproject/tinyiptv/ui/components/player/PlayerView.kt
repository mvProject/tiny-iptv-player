/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:27
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import android.view.SurfaceView
import android.view.ViewGroup
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
import androidx.media3.common.Player
import com.mvproject.tinyiptv.data.enums.player.PlayerCommands
import com.mvproject.tinyiptv.data.enums.player.PlayerUICommands
import com.mvproject.tinyiptv.data.enums.player.ViewSettingsRequest
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.modifiers.adaptiveLayout
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerTapGestures
import com.mvproject.tinyiptv.ui.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerState: VideoViewViewModel.ControlUIState,
    programs: List<EpgProgram>,
    player: Player,
    onViewSettingsAction: (action: ViewSettingsRequest) -> Unit = {},
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {},
) {

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .defaultPlayerHorizontalGestures(onPlayerCommand = onPlayerCommand)
            .defaultPlayerVerticalGestures(onAction = onViewSettingsAction)
            .defaultPlayerTapGestures(onPlayerUICommand = onPlayerUICommand),
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
                    // player
                    //     .trackSelectionParameters
                    //     .buildUpon()
                    //     .setMaxVideoSizeSd()
                    //     .build()
                }//
            }
        )

        PlayerControlsContainer(
            modifier = Modifier.fillMaxSize(),
            playerState = playerState,
            programs = programs,
            onPlayerCommand = onPlayerCommand,
            onPlayerUICommand = onPlayerUICommand
        )
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