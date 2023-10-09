/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 10:56
 *
 */

package com.mvproject.tinyiptv.ui.components.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.PreviewTestData
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.ui.screens.settings.playlist.action.SettingsPlaylistAction
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun PlaylistItemView(
    modifier: Modifier = Modifier,
    item: Playlist,
    onSelect: () -> Unit = {},
    onPlaylistAction: (SettingsPlaylistAction) -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .clickable {
                        onSelect()
                    }
            ) {
                Text(
                    text = item.playlistTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size4))

                Text(
                    text = item.playlistUrl,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            FilledIconButton(
                onClick = {
                    onPlaylistAction(SettingsPlaylistAction.DeletePlaylist(item))
                },
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Playlist Delete",
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistItemView() {
    VideoAppTheme() {
        PlaylistItemView(
            item = PreviewTestData.testPlaylist
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistsItemView() {
    VideoAppTheme(darkTheme = true) {
        PlaylistItemView(
            item = PreviewTestData.testPlaylist
        )
    }
}