/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsView
import com.mvproject.tinyiptv.ui.screens.channels.TvPlaylistChannelsViewModel
import org.koin.androidx.compose.koinViewModel

data class PlaylistGroupScreenRoute(
    val group: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val tvPlaylistChannelsViewModel: TvPlaylistChannelsViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow

        TvPlaylistChannelsView(
            viewModel = tvPlaylistChannelsViewModel,
            groupSelected = group,
            onAction = tvPlaylistChannelsViewModel::processAction,
            onNavigateBack = {
                navigator.pop()
            },
            onNavigateSelected = { url ->
                navigator.push(
                    PlayerScreenRoute(
                        mediaUrl = url,
                        mediaGroup = group
                    )
                )
            }
        )
    }
}