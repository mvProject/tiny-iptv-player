/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.10.23, 11:14
 *
 */

package com.mvproject.tinyiptv.ui.screens.player.events

sealed class PlaybackEvents {
    data object OnPlaybackRestart : PlaybackEvents()
    data object OnPlaybackToggle : PlaybackEvents()
    data object OnVideoResizeToggle : PlaybackEvents()
    data object OnFullScreenToggle : PlaybackEvents()
    data object OnPlayerUiToggle : PlaybackEvents()
    data object OnEpgUiToggle : PlaybackEvents()
    data object OnChannelsUiToggle : PlaybackEvents()
    data object OnChannelInfoUiToggle : PlaybackEvents()
    data object OnNextSelected : PlaybackEvents()
    data object OnPreviousSelected : PlaybackEvents()

    data object OnVolumeUp : PlaybackEvents()
    data object OnVolumeDown : PlaybackEvents()
}