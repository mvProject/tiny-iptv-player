/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.presentation.playlist.AddPlayListViewModel
import com.mvproject.videoapp.presentation.playlist.PlayListView
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