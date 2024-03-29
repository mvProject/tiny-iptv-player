/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:15
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.epg.PlayerChannelEpgItem
import com.mvproject.tinyiptv.ui.components.modifiers.roundedHeader
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants
import kotlin.time.Duration.Companion.minutes

@Composable
fun PlayerChannelView(
    modifier: Modifier = Modifier,
    currentChannel: TvPlaylistChannel,
    programCount: Int = AppConstants.INT_VALUE_1,
    isPlaying: Boolean = false,
    isFullScreen: Boolean = false,
    onPlaybackAction: (PlaybackActions) -> Unit = {}
) {
    Box(
        modifier = modifier
            .alpha(MaterialTheme.dimens.alpha80)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .roundedHeader(
                    color = MaterialTheme.colorScheme.primary
                )
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = currentChannel.channelName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

            if (currentChannel.channelEpg.isNotEmpty()) {
                currentChannel.channelEpg.toActual().take(programCount).forEach { epg ->
                    PlayerChannelEpgItem(epgProgram = epg)
                }
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))
            }

            PlayerControlView(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(MaterialTheme.dimens.alpha70),
                isFavorite = currentChannel.isInFavorites,
                isPlaying = isPlaying,
                isFullScreen = isFullScreen,
                onPlaybackAction = onPlaybackAction
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerChannelView() {
    VideoAppTheme(darkTheme = true) {
        PlayerChannelView(
            currentChannel = TvPlaylistChannel(
                channelName = "Test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg Title",
                        channelId = "id",
                        start = System.currentTimeMillis() - 30.minutes.inWholeMilliseconds,
                        stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                        description = "Epg Description"
                    )
                )
            )
        )
    }
}