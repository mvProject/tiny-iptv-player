/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:39
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.PreviewTestData.testProgram
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.tinyiptv.ui.components.epg.ScheduleEpgItemView
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg,
    onEpgRequest: (PlaylistChannelWithEpg) -> Unit = {},
    onFavoriteClick: (PlaylistChannelWithEpg) -> Unit = {}
) {
    LaunchedEffect(key1 = channel.id) {
        onEpgRequest(channel)
    }

    Card(
        modifier = modifier
            .heightIn(MaterialTheme.dimens.size140)
            .clip(MaterialTheme.shapes.extraSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = modifier
                    .padding(MaterialTheme.dimens.size8),
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
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(MaterialTheme.dimens.weight5)
                )

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
            Row(
                modifier = modifier
                    .padding(horizontal = MaterialTheme.dimens.size8),
                verticalAlignment = Alignment.Top
            ) {
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = MaterialTheme.dimens.size4),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelGridView() {
    VideoAppTheme() {
        ChannelGridView(channel = testProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewChannelGridView() {
    VideoAppTheme(darkTheme = true) {
        ChannelGridView(channel = testProgram)
    }
}