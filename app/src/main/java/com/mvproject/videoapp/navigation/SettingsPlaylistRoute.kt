/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.ui.screens.settings.actions.SettingsPlaylistAction
import com.mvproject.videoapp.ui.screens.settings.view.SettingsPlaylistView
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsPlaylistViewModel
import org.koin.androidx.compose.koinViewModel

class SettingsPlaylistRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val settingsPlaylistViewModel: SettingsPlaylistViewModel = koinViewModel()
        val playlistDataState by settingsPlaylistViewModel.playlistDataState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SettingsPlaylistView(
            dataState = playlistDataState,
            onPlaylistAction = { action ->
                when (action) {
                    is SettingsPlaylistAction.NewPlaylist ->
                        navigator.push(PlaylistDetailRoute())

                    is SettingsPlaylistAction.SelectPlaylist ->
                        navigator.push(PlaylistDetailRoute(id = action.id))

                    is SettingsPlaylistAction.DeletePlaylist ->
                        settingsPlaylistViewModel.deletePlaylist(action.id)

                    is SettingsPlaylistAction.NavigateBack -> navigator.pop()
                }
            }
        )
    }
}