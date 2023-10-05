/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.groups.GroupView
import com.mvproject.tinyiptv.ui.screens.groups.GroupViewModel
import org.koin.androidx.compose.koinViewModel

class PlaylistDataRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val groupViewModel: GroupViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val playlistDataState by groupViewModel.playlistDataState.collectAsState()

        //  LaunchedEffect(key1 = groupViewModel) {
        //      Napier.e("testing PlaylistDataView LaunchedEffect")
        //      groupViewModel.dataInit()
        //  }

        GroupView(
            dataState = playlistDataState,
            onNavigateToSettings = {
                navigator.push(SettingsRoute())
            },
            onNavigateToGroup = {
                navigator.push(
                    PlaylistGroupScreenRoute(group = it)
                )
            },
            onPlaylistAction = groupViewModel::processAction
        )
    }
}