/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 12.05.23, 20:08
 *
 */

package com.mvproject.videoapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistDataView() {
    val playlistDataViewModel: PlaylistDataViewModel = koinViewModel()
    val playlistDataState by playlistDataViewModel.playlistDataState.collectAsState()
    val playlistState by playlistDataViewModel.playlistState.collectAsState()

    PlaylistDataContentView(
        dataState = playlistDataState,
        playlistState = playlistState,
        playlistDataViewModel::changePlaylist
    )
}