/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:15
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.PreviewTestData.testProgram
import com.mvproject.tinyiptv.data.mappers.ListMappers.toActual
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.components.epg.ScheduleEpgItemView
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .height(MaterialTheme.dimens.size140)
            .clip(MaterialTheme.shapes.extraSmall)
            .combinedClickable(
                onClick = onChannelSelect,
                onLongClick = onOptionSelect
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimens.size8,
                        vertical = MaterialTheme.dimens.size4
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(channel.channelLogo)
                        .crossfade(true)
                        .placeholder(R.drawable.no_channel_logo)
                        .error(R.drawable.no_channel_logo)
                        .build(),
                    contentDescription = channel.channelName,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size42)
                        .clip(MaterialTheme.shapes.small)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

                Text(
                    text = channel.channelName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (channel.isInFavorites)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight5)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size10))

            if (channel.channelEpg.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.msg_no_epg_found),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.size8),
                        textAlign = TextAlign.Center
                    )
                }

            } else {
                // todo epg count view
                channel.channelEpg.toActual().take(1)
                    .forEach {
                        ScheduleEpgItemView(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimens.size4),
                            program = it
                        )
                    }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelGridView() {
    VideoAppTheme(darkTheme = true) {
        ChannelGridView(channel = testProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelGridViewFavorite() {
    VideoAppTheme(darkTheme = true) {
        ChannelGridView(channel = testProgram.copy(isInFavorites = true))
    }
}