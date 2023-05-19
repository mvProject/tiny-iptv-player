/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.PreviewTestData
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun ChannelCardView(
    modifier: Modifier = Modifier,
    channel: PlaylistChannelWithEpg
) {
    Card(
        modifier = modifier
            .heightIn(MaterialTheme.dimens.size200)
            .clip(MaterialTheme.shapes.extraSmall),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
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
                contentDescription = channel.channelName,
                modifier = Modifier
                    .size(MaterialTheme.dimens.size112)
                    .clip(MaterialTheme.shapes.extraSmall)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Text(
                text = channel.channelName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewChannelCardView() {
    VideoAppTheme() {
        ChannelCardView(channel = PreviewTestData.testProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewChannelCardView() {
    VideoAppTheme(darkTheme = true) {
        ChannelCardView(channel = PreviewTestData.testProgram)
    }
}