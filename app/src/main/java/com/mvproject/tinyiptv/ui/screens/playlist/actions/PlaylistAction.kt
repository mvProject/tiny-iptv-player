/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 11:30
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.actions

sealed class PlaylistAction {
    data class SelectPlaylist(val id: Int) : PlaylistAction()
    data class SelectGroup(val group: String) : PlaylistAction()
    object NavigateToSettings : PlaylistAction()
    object NavigateToPlaylist : PlaylistAction()
}

