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
import com.mvproject.tinyiptv.ui.screens.playlist.actions.PlaylistDetailAction
import com.mvproject.tinyiptv.ui.screens.playlist.view.PlaylistDetailView
import com.mvproject.tinyiptv.ui.screens.playlist.viewmodel.AddPlayListViewModel
import com.mvproject.tinyiptv.utils.AppConstants
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.koinViewModel

data class PlaylistDetailRoute(
    val id: String = AppConstants.EMPTY_STRING
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val addPlayListViewModel: AddPlayListViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(key1 = id) {
            Napier.w("testing PlaylistDetailRoute playlistId:$id")
            addPlayListViewModel.setPlaylistMode(id)
        }

        val state by addPlayListViewModel.state.collectAsState()

        PlaylistDetailView(
            state = state,
            onPlaylistAction = { action ->
                when (action) {
                    is PlaylistDetailAction.NavigateBack -> {
                        navigator.pop()
                    }

                    else -> addPlayListViewModel.processAction(action)

                }
            }
        )
    }
}