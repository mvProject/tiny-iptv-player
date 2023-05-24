/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:28
 *
 */

package com.mvproject.videoapp.ui.components.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.components.views.BrightnessProgressView
import com.mvproject.videoapp.ui.components.views.LoadingView
import com.mvproject.videoapp.ui.components.views.NoPlaybackView
import com.mvproject.videoapp.ui.components.views.VolumeProgressView
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import com.mvproject.videoapp.utils.calculateAudioProgress
import com.mvproject.videoapp.utils.calculateBrightnessProgress

@Composable
fun PlayerControlsContainer(
    modifier: Modifier,
    playerState: VideoViewViewModel.ControlUIState,
    programs: List<EpgProgram>,
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) {
    AnimatedVisibility(
        visible = !playerState.isOnline,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
        ) {
            NoPlaybackView(
                textRes = R.string.msg_no_internet_found,
                iconRes = R.drawable.ic_no_network
            )
        }
    }

    AnimatedVisibility(
        visible = !playerState.isMediaPlayable,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
        ) {
            NoPlaybackView(
                textRes = R.string.msg_no_playable_media_found,
                iconRes = R.drawable.ic_sad_face
            )
        }
    }

    AnimatedVisibility(
        visible = playerState.isBrightnessChanging,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        val currentValue = remember(playerState.brightnessValue) {
            derivedStateOf {
                playerState.brightnessValue.calculateBrightnessProgress()
            }
        }

        BrightnessProgressView(
            modifier = modifier,
            value = { currentValue.value }
        )
    }

    AnimatedVisibility(
        visible = playerState.isVolumeChanging,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val currentValue = remember(playerState.volumeValue) {
            derivedStateOf {
                playerState.volumeValue.calculateAudioProgress()
            }
        }
        VolumeProgressView(
            modifier = modifier,
            value = { currentValue.value }
        )
    }

    AnimatedVisibility(
        visible = playerState.isBuffering,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoadingView()
    }

    AnimatedVisibility(
        visible = playerState.isUiVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        PlayerChannelView(
            modifier = modifier,
            channelName = playerState.currentChannel,
            epgPrograms = programs,
            playerState = playerState,
            onPlayerCommand = onPlayerCommand,
            onPlayerUICommand = onPlayerUICommand
        )
    }
}