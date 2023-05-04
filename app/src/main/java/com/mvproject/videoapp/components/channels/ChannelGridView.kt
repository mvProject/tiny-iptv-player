/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.videoapp.PreviewTestData.testProgram
import com.mvproject.videoapp.R
import com.mvproject.videoapp.components.epg.ScheduleEpgView
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.ui.theme.dimens
import io.github.aakira.napier.Napier

@Composable
fun ChannelGridView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg,
    onEpgRequest: (PlaylistChannelWithEpg) -> Unit = {}
) {

    SideEffect {
        Napier.e("testing PlaylistGroupDataItemView SideEffect item ${channel.channelName}")
    }

    LaunchedEffect(key1 = channel.id) {
        onEpgRequest(channel)
    }

    Column(
        modifier = modifier
            .heightIn(140.dp)
            .padding(MaterialTheme.dimens.size4),
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
                contentDescription = null,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size42)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
            )

            Spacer(
                modifier = Modifier
                    .width(MaterialTheme.dimens.size4)
            )

            Text(
                text = channel.channelName,
                fontSize = 16.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size4),
                color = MaterialTheme.colors.onBackground
            )
        }
        Row(
            modifier = modifier
                .padding(horizontal = MaterialTheme.dimens.size8),
            verticalAlignment = Alignment.Top
        ) {
            channel.channelEpg.forEach {
                ScheduleEpgView(
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
                        .fillMaxWidth()
                        .padding(start = MaterialTheme.dimens.size4),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelGridView() {
    ChannelGridView(channel = testProgram)
}