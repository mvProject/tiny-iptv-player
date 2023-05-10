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
import com.mvproject.videoapp.ui.screens.playlist.AddPlayListViewModel
import com.mvproject.videoapp.ui.screens.playlist.PlayListView
import org.koin.androidx.compose.getViewModel

data class PlaylistScreenRoute(
    val id: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val addPlayListViewModel: AddPlayListViewModel = getViewModel()
        addPlayListViewModel.setPlaylistMode(id)
        val state by addPlayListViewModel.state.collectAsState()

        PlayListView(
            playlistId = id,
            state = state,
            onPlaylistAction = addPlayListViewModel::processAction
        )
    }
}