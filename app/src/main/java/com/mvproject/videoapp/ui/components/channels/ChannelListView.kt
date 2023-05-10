/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 20:21
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.channels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.PreviewTestData.testProgram
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.ui.components.epg.ScheduleEpgItemView
import com.mvproject.videoapp.ui.theme.dimens
import io.github.aakira.napier.Napier

@Composable
fun ChannelListView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg,
    onEpgRequest: (PlaylistChannelWithEpg) -> Unit = {},
    onFavoriteClick: (PlaylistChannelWithEpg) -> Unit = {}
) {
    SideEffect {
        Napier.e("testing PlaylistGroupDataItemView SideEffect item ${channel.channelName}")
    }

    LaunchedEffect(key1 = channel.id) {
        onEpgRequest(channel)
    }

    Row(
        modifier = modifier
            .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(channel.channelLogo)
                .crossfade(true)
                .placeholder(R.drawable.no_channel_logo)
                .error(R.drawable.no_channel_logo)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(MaterialTheme.dimens.size42)
                .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
        )

        Spacer(
            modifier = Modifier
                .width(MaterialTheme.dimens.size4)
        )

        Column(
            Modifier.weight(MaterialTheme.dimens.weight5)
        ) {
            Text(
                text = channel.channelName,
                fontSize = 16.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.size4),
                color = MaterialTheme.colors.onBackground
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
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(start = MaterialTheme.dimens.size4),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        Spacer(
            modifier = Modifier
                .width(MaterialTheme.dimens.size4)
        )

        IconButton(
            modifier = Modifier.weight(MaterialTheme.dimens.weight1),
            onClick = { onFavoriteClick(channel) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "PLAYBACK_TOGGLE",
                tint = if (channel.isInFavorites) Color.Red else Color.DarkGray
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewChannelListView() {
    ChannelListView(channel = testProgram)
}