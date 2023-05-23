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
import com.mvproject.videoapp.ui.screens.playlist.actions.PlaylistAction
import com.mvproject.videoapp.ui.screens.playlist.view.PlaylistDataView
import com.mvproject.videoapp.ui.screens.playlist.viewmodel.PlaylistDataViewModel
import org.koin.androidx.compose.koinViewModel

class PlaylistDataRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val playlistDataViewModel: PlaylistDataViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val playlistDataState by playlistDataViewModel.playlistDataState.collectAsState()

        PlaylistDataView(
            dataState = playlistDataState,
            onPlaylistAction = { action ->
                when (action) {
                    is PlaylistAction.NavigateToSettings -> {
                        navigator.push(SettingsRoute())
                    }

                    is PlaylistAction.NavigateToPlaylist -> {
                        navigator.push(PlaylistDetailRoute())
                    }

                    is PlaylistAction.SelectGroup -> {
                        navigator.push(
                            PlaylistGroupScreenRoute(group = action.group)
                        )
                    }

                    is PlaylistAction.SelectPlaylist -> {
                        playlistDataViewModel.changePlaylist(action.id)
                    }
                }
            }
        )
    }
}