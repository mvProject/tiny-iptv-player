/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.ui.screens.playlist.AddPlayListViewModel
import com.mvproject.videoapp.ui.screens.playlist.PlaylistDetailView
import com.mvproject.videoapp.utils.AppConstants
import org.koin.androidx.compose.koinViewModel

data class PlaylistDetailRoute(
    val id: String = AppConstants.EMPTY_STRING
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val addPlayListViewModel: AddPlayListViewModel = koinViewModel()

        PlaylistDetailView(
            playlistId = id,
            viewModel = addPlayListViewModel
        )
    }
}