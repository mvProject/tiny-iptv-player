/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:15
 *
 */

package com.mvproject.tinyiptv.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.channels.ChannelListView
import com.mvproject.tinyiptv.ui.components.modifiers.fullScreenWidth
import com.mvproject.tinyiptv.ui.components.modifiers.roundedHeader
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_ZERO
import kotlin.time.Duration.Companion.minutes

@Composable
fun OverlayChannels(
    isFullScreen: Boolean = false,
    group: String,
    channels: List<TvPlaylistChannel>,
    current: Int = INT_VALUE_ZERO,
    onChannelSelect: (TvPlaylistChannel) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = current) {
        listState.animateScrollToItem(current)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(MaterialTheme.dimens.fraction90)
            .fullScreenWidth(enabled = isFullScreen)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .roundedHeader(),
            text = group,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
            contentPadding = PaddingValues(
                vertical = MaterialTheme.dimens.size4,
                horizontal = MaterialTheme.dimens.size2
            ),
            content = {
                items(
                    items = channels,
                    key = { it.channelUrl }
                ) { chn ->
                    ChannelListView(
                        channel = chn,
                        onChannelSelect = { onChannelSelect(chn) }
                    )
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewOOverlayChannels() {
    VideoAppTheme(darkTheme = true) {
        OverlayChannels(
            group = "TestGroup",
            channels = listOf(
                TvPlaylistChannel(
                    channelName = "test1",
                    channelEpg = listOf(
                        EpgProgram(
                            title = "Epg1",
                            channelId = "id1",
                            start = System.currentTimeMillis() - 15.minutes.inWholeMilliseconds,
                            stop = System.currentTimeMillis() + 15.minutes.inWholeMilliseconds,
                            description = "Epg Description"
                        )
                    )
                ),
                TvPlaylistChannel(
                    channelName = "test2",
                    channelEpg = listOf(
                        EpgProgram(
                            title = "Epg2",
                            channelId = "id2",
                            start = System.currentTimeMillis() + 15.minutes.inWholeMilliseconds,
                            stop = System.currentTimeMillis() + 30.minutes.inWholeMilliseconds,
                            description = "Epg Description"
                        )
                    )
                )
            )
        )
    }
}