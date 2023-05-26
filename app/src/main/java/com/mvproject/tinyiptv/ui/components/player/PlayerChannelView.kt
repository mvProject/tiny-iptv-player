/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:28
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.PreviewTestData.testEpgPrograms
import com.mvproject.tinyiptv.data.enums.player.PlayerCommands
import com.mvproject.tinyiptv.data.enums.player.PlayerUICommands
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.epg.PlayerEpgItemView
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1

@Composable
fun PlayerChannelView(
    modifier: Modifier = Modifier,
    channelName: String,
    epgPrograms: List<EpgProgram>,
    programCount: Int = INT_VALUE_1,
    playerState: VideoViewViewModel.ControlUIState,
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) {
    Box(
        modifier = modifier
            .alpha(MaterialTheme.dimens.alpha70)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = MaterialTheme.dimens.size16,
                        topEnd = MaterialTheme.dimens.size16
                    )
                )
                .align(Alignment.BottomCenter)
                .padding(MaterialTheme.dimens.size4),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = channelName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

            epgPrograms.take(programCount).forEach { epg ->
                PlayerEpgItemView(epgProgram = epg)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

            PlayerControlView(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(MaterialTheme.dimens.alpha70),
                playerState = playerState,
                onPlayerCommand = onPlayerCommand,
                onPlayerUICommand = onPlayerUICommand
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerChannelView() {
    VideoAppTheme() {
        PlayerChannelView(
            epgPrograms = testEpgPrograms,
            channelName = "Current Channel",
            playerState = VideoViewViewModel.ControlUIState(50, 15)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerChannelView() {
    VideoAppTheme(darkTheme = true) {
        PlayerChannelView(
            epgPrograms = testEpgPrograms,
            channelName = "Current Channel",
            playerState = VideoViewViewModel.ControlUIState(50, 15)
        )
    }
}