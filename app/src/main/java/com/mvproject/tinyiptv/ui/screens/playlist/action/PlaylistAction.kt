/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 11:30
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.action

sealed class PlaylistAction {
    data class SetTitle(val title: String) : PlaylistAction()
    data class SetRemoteUrl(val url: String) : PlaylistAction()
    data class SetLocalUri(val uri: String) : PlaylistAction()
    data class SetUpdatePeriod(val period: Int) : PlaylistAction()
    data object SavePlaylist : PlaylistAction()
    data object UpdatePlaylist : PlaylistAction()
}

