/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:46
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.components.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.ui.theme.dimens
import io.github.aakira.napier.Napier


@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg
) {
    SideEffect {
        Napier.e("testing PlaylistGroupDataItemView SideEffect item ${channel.channelName}")
    }

    Column(
        modifier = modifier
            .heightIn(200.dp)
            .padding(MaterialTheme.dimens.size4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
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
                .size(MaterialTheme.dimens.size112)
                .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
        )

        Spacer(
            modifier = Modifier
                .height(MaterialTheme.dimens.size12)
        )

        Text(
            text = channel.channelName,
            fontSize = 18.sp,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.size4),
            color = MaterialTheme.colors.onBackground,
        )
    }
}