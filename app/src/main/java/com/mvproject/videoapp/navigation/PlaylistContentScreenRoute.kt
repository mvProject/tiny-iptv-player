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
import com.mvproject.videoapp.ui.screens.PlaylistDataViewModel
import com.mvproject.videoapp.ui.screens.PlaylistGroupContent
import org.koin.androidx.compose.getViewModel

class PlaylistContentScreenRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val playlistDataViewModel: PlaylistDataViewModel = getViewModel()
        val playlistDataState by playlistDataViewModel.playlistDataState.collectAsState()
        val playlistState by playlistDataViewModel.playlistState.collectAsState()

        PlaylistGroupContent(
            dataState = playlistDataState,
            playlistState = playlistState,
            playlistDataViewModel::changePlaylist
        )
    }
}