/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.ui.screens.playlist.actions.PlaylistGroupAction
import com.mvproject.videoapp.ui.screens.playlist.view.PlaylistGroupView
import com.mvproject.videoapp.ui.screens.playlist.viewmodel.PlaylistGroupViewModel
import org.koin.androidx.compose.koinViewModel

data class PlaylistGroupScreenRoute(
    val group: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val playlistGroupViewModel: PlaylistGroupViewModel = koinViewModel()
        val navigator = LocalNavigator.currentOrThrow

        PlaylistGroupView(
            viewModel = playlistGroupViewModel,
            groupSelected = group,
            onGroupAction = { action ->
                when (action) {
                    is PlaylistGroupAction.NavigateBack -> {
                        navigator.pop()
                    }

                    is PlaylistGroupAction.NavigateToGroup -> {
                        navigator.push(
                            PlayerScreenRoute(
                                mediaId = action.id,
                                mediaGroup = group
                            )
                        )
                    }
                }
            }
        )
    }
}