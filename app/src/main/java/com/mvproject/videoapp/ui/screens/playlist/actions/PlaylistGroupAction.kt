/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 11:30
 *
 */

package com.mvproject.videoapp.ui.screens.playlist.actions

sealed class PlaylistGroupAction {
    data class NavigateToGroup(val id: String) : PlaylistGroupAction()
    object NavigateBack : PlaylistGroupAction()
}

