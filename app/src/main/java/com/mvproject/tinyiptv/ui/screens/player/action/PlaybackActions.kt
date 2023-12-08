/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 12:37
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.action

sealed class PlaybackActions {

    data object OnPlaybackToggle : PlaybackActions()
    data object OnVideoResizeToggle : PlaybackActions()
    data object OnVideoRatioToggle : PlaybackActions()
    data object OnFullScreenToggle : PlaybackActions()
    data object OnFavoriteToggle : PlaybackActions()
    data object OnPlayerUiToggle : PlaybackActions()
    data object OnEpgUiToggle : PlaybackActions()
    data object OnChannelsUiToggle : PlaybackActions()
    data object OnChannelInfoUiToggle : PlaybackActions()
    data object OnNextSelected : PlaybackActions()
    data object OnPreviousSelected : PlaybackActions()
    data object OnVolumeUp : PlaybackActions()
    data object OnVolumeDown : PlaybackActions()
    data object OnRestarted : PlaybackActions()
}