/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistView
import com.mvproject.tinyiptv.ui.screens.playlist.PlaylistViewModel
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.koinViewModel

data class PlaylistDetailRoute(
    val id: String = AppConstants.EMPTY_STRING
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val playlistViewModel: PlaylistViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = id) {
            Napier.w("testing PlaylistDetailRoute playlistId:$id")
            playlistViewModel.setPlaylistMode(id)
        }

        val state by playlistViewModel.state.collectAsState()

        PlaylistView(
            state = state,
            onPlaylistAction = playlistViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            }
        )
    }
}