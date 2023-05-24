/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun PlayerControlView(
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
            onClick = { onPlayerCommand(PlayerCommands.PLAYBACK_TOGGLE) },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
        ) {
            Icon(
                imageVector = if (playerState.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "PLAYBACK_TOGGLE",
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onPlayerUICommand(PlayerUICommands.TOGGLE_RESIZE_MODE) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Crop,
                    contentDescription = "TOGGLE_RESIZE_MODE",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            IconButton(
                onClick = { onPlayerUICommand(PlayerUICommands.TOGGLE_FULL_SCREEN) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Icon(
                    imageVector = if (playerState.isFullscreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                    contentDescription = "TOGGLE_FULL_SCREEN",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerControls() {
    VideoAppTheme() {
        PlayerControlView(
            playerState = VideoViewViewModel.ControlUIState(50, 15)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerControls() {
    VideoAppTheme(darkTheme = true) {
        PlayerControlView(
            playerState = VideoViewViewModel.ControlUIState(50, 15)
        )
    }
}