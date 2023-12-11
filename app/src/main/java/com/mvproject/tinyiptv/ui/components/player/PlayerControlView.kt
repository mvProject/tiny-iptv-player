/*
 *  Created by Medvediev Viktor [mvproject] 
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:31
 *
 */

package com.mvproject.tinyiptv.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.Crop
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.FullscreenExit
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mvproject.tinyiptv.ui.components.views.PlaybackControl
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun PlayerControlView(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isFullScreen: Boolean,
    onPlaybackAction: (PlaybackActions) -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        PlaybackControl(
            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            action = { onPlaybackAction(PlaybackActions.OnPlaybackToggle) }
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            PlaybackControl(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                action = { onPlaybackAction(PlaybackActions.OnFavoriteToggle) }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            PlaybackControl(
                imageVector = Icons.Rounded.AspectRatio,
                action = { onPlaybackAction(PlaybackActions.OnVideoRatioToggle) }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            PlaybackControl(
                imageVector = Icons.Rounded.Crop,
                action = { onPlaybackAction(PlaybackActions.OnVideoResizeToggle) }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimens.size8))

            PlaybackControl(
                imageVector = if (isFullScreen) Icons.Rounded.FullscreenExit else Icons.Rounded.Fullscreen,
                action = { onPlaybackAction(PlaybackActions.OnFullScreenToggle) }
            )
        }
    }
}