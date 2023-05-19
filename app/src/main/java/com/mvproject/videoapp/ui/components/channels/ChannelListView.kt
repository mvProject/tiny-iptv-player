/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:39
 *
 */

package com.mvproject.videoapp.ui.components.channels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.PreviewTestData.testProgram
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.ui.components.epg.ScheduleEpgItemView
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg,
    onEpgRequest: (PlaylistChannelWithEpg) -> Unit = {},
    onFavoriteClick: (PlaylistChannelWithEpg) -> Unit = {}
) {
    LaunchedEffect(key1 = channel.id) {
        onEpgRequest(channel)
    }

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.small),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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

            Column(
                Modifier.weight(MaterialTheme.dimens.weight5)
            ) {
                Text(
                    text = channel.channelName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                channel.channelEpg.forEach {
                    ScheduleEpgItemView(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimens.size4),
                        program = it
                    )
                }

                if (channel.channelEpg.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.msg_no_epg_found),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size4))

            IconButton(
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .clip(MaterialTheme.shapes.small),
                onClick = { onFavoriteClick(channel) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorites",
                    tint = if (channel.isInFavorites)
                        MaterialTheme.colorScheme.tertiary else
                        MaterialTheme.colorScheme.tertiaryContainer
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewChannelListView() {
    VideoAppTheme() {
        ChannelListView(channel = testProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelListViewFav() {
    VideoAppTheme() {
        ChannelListView(channel = testProgram.copy(isInFavorites = true))
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewChannelListView() {
    VideoAppTheme(darkTheme = true) {
        ChannelListView(channel = testProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewChannelListViewFav() {
    VideoAppTheme(darkTheme = true) {
        ChannelListView(channel = testProgram.copy(isInFavorites = true))
    }
}