/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 11:30
 *
 */

package com.mvproject.videoapp.ui.screens.playlist.actions

sealed class PlaylistDetailAction {
    data class ChangeName(val newName: String) : PlaylistDetailAction()
    data class ChangeUrl(val newUrl: String) : PlaylistDetailAction()
    data class ChangeUpdatePeriod(val period: Int) : PlaylistDetailAction()
    data class ChangeMainEpgUsingState(val state: Boolean) : PlaylistDetailAction()
    data class ChangeAlterEpgUsingState(val state: Boolean) : PlaylistDetailAction()
    object SavePlaylist : PlaylistDetailAction()
    object NewPlaylist : PlaylistDetailAction()
    object NavigateBack : PlaylistDetailAction()
}

