/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 15:38
 *
 */

package com.mvproject.tinyiptv.ui.components.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.models.channels.ChannelsGroup
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants

@Composable
fun PlaylistGroupItemView(
    modifier: Modifier = Modifier,
    group: ChannelsGroup
) {
    Row(
        modifier = modifier
            .wrapContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = group.groupName,
            modifier = Modifier
                .size(MaterialTheme.dimens.size42)
                .clip(MaterialTheme.shapes.small),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

        Text(
            text = group.groupName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight5)
        )

        if (group.groupContentCount > AppConstants.INT_VALUE_ZERO) {
            Text(
                text = group.groupContentCount.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistGroupItemView() {
    VideoAppTheme() {
        PlaylistGroupItemView(
            group = ChannelsGroup("testName", groupContentCount = 99)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistGroupItemView() {
    VideoAppTheme(darkTheme = true) {
        PlaylistGroupItemView(
            group = ChannelsGroup("testName", groupContentCount = 99)
        )
    }
}