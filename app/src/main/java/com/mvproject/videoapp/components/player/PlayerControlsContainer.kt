/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.videoapp.R
import com.mvproject.videoapp.components.errors.NoPlaybackView
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.presentation.player.VideoViewViewModel
import com.mvproject.videoapp.utils.calculateAudioProgress
import com.mvproject.videoapp.utils.calculateBrightnessProgress
import io.github.aakira.napier.Napier

@Composable
fun PlayerControlsContainer(
    modifier: Modifier,
    contentColor: Color = Color.White,
    playerState: VideoViewViewModel.ControlUIState,
    programs: List<EpgProgram>,
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) {

    SideEffect {
        Napier.d("testing PlayerControlsContainer SideEffect")
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {

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
            Box(
                modifier = modifier
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        AnimatedVisibility(
            visible = playerState.isUiVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            PlayerChannelView(
                modifier = modifier,
                channelName = playerState.currentChannel,
                program = programs,
                playerState = playerState,
                onPlayerCommand = onPlayerCommand,
                onPlayerUICommand = onPlayerUICommand
            )
        }
    }
}