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
import com.mvproject.videoapp.presentation.settings.view.SettingsPlaylistView
import com.mvproject.videoapp.presentation.settings.viewmodel.SettingsPlaylistViewModel
import org.koin.androidx.compose.getViewModel

class SettingsScreenPlaylistRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val settingsPlaylistViewModel: SettingsPlaylistViewModel = getViewModel()
        val playlistDataState by settingsPlaylistViewModel.playlistDataState.collectAsState()

        SettingsPlaylistView(
            dataState = playlistDataState,
            onDeleteItem = settingsPlaylistViewModel::deletePlaylist
        )
    }
}