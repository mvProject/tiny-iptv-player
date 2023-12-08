/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:15
 *
 */

package com.mvproject.tinyiptv.ui.components.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptv.ui.components.modifiers.fullScreenWidth
import com.mvproject.tinyiptv.ui.components.modifiers.roundedHeader
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import kotlin.time.Duration.Companion.minutes

@Composable
fun OverlayEpg(
    isFullScreen: Boolean = false,
    currentChannel: TvPlaylistChannel
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(MaterialTheme.dimens.fraction90)
            .fullScreenWidth(enabled = isFullScreen)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .roundedHeader(),
            text = currentChannel.channelName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        PlayerEpgContent(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    bottomStart = MaterialTheme.dimens.size8,
                    bottomEnd = MaterialTheme.dimens.size8
                )
            ),
            epgList = currentChannel.channelEpg
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewOverlayEpg() {
    VideoAppTheme(darkTheme = true) {
        OverlayEpg(
            currentChannel = TvPlaylistChannel(
                channelName = "test",
                channelEpg = listOf(
                    EpgProgram(
                        title = "Epg",
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
