/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:26
 *
 */

package com.mvproject.videoapp.ui.screens.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.github.aakira.napier.Napier

@Composable
fun PlaylistDetailView(playlistId: String, viewModel: AddPlayListViewModel) {
    LaunchedEffect(key1 = playlistId) {
        Napier.w("testing PlayListView playlistId:$playlistId")
        viewModel.setPlaylistMode(playlistId)
    }

    val state by viewModel.state.collectAsState()

    PlaylistDetailViewContent(
        playlistId = playlistId,
        state = state,
        onPlaylistAction = viewModel::processAction
    )
}