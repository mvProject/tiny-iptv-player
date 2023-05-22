/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 11:30
 *
 */

package com.mvproject.videoapp.ui.screens.settings.actions

sealed class SettingsPlaylistAction {
    data class SelectPlaylist(val id: String) : SettingsPlaylistAction()
    data class DeletePlaylist(val id: String) : SettingsPlaylistAction()
    object NewPlaylist : SettingsPlaylistAction()
    object NavigateBack : SettingsPlaylistAction()
}

