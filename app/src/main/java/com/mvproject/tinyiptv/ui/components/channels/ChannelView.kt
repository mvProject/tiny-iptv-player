/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 15:35
 *
 */

package com.mvproject.tinyiptv.ui.components.channels

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    item: TvPlaylistChannel,
    onChannelSelect: () -> Unit = {},
    onOptionSelect: () -> Unit = {}
) {
    when (viewType) {
        ChannelsViewType.LIST -> {
            ChannelListView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect
            )
        }

        ChannelsViewType.GRID -> {
            ChannelGridView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect
            )
        }

        ChannelsViewType.CARD -> {
            ChannelCardView(
                modifier = modifier,
                channel = item,
                onChannelSelect = onChannelSelect,
                onOptionSelect = onOptionSelect
            )
        }
    }
}