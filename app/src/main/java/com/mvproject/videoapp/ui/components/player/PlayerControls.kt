/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 20:20
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material.icons.outlined.SubtitlesOff
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.MusicOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    playerState: VideoViewViewModel.ControlUIState,
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = { onPlayerCommand(PlayerCommands.PLAYBACK_TOGGLE) }
        ) {
            Icon(
                imageVector = if (playerState.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "PLAYBACK_TOGGLE",
                tint = Color.LightGray
            )
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onPlayerUICommand(PlayerUICommands.TOGGLE_RESIZE_MODE) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Crop,
                    contentDescription = "TOGGLE_RESIZE_MODE",
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = if (playerState.isUseSubtitle) Icons.Outlined.Subtitles else Icons.Outlined.SubtitlesOff,
                    contentDescription = "Subtitle toggle",
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = if (playerState.isTracksAvailable) Icons.Rounded.MusicNote else Icons.Rounded.MusicOff,
                    contentDescription = "Audio track toggle",
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = { onPlayerUICommand(PlayerUICommands.TOGGLE_FULL_SCREEN) }
            ) {
                Icon(
                    imageVector = if (playerState.isFullscreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                    contentDescription = "TOGGLE_FULL_SCREEN",
                    tint = Color.LightGray
                )
            }
        }
    }
}