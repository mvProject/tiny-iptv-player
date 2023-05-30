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
import com.mvproject.tinyiptv.ui.screens.settings.actions.SettingsPlayerAction
import com.mvproject.tinyiptv.ui.screens.settings.view.SettingsPlayerView
import com.mvproject.tinyiptv.ui.screens.settings.viewmodel.SettingsPlayerViewModel
import org.koin.androidx.compose.koinViewModel

object SettingsPlayerRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val settingsPlayerViewModel: SettingsPlayerViewModel = koinViewModel()
        val state by settingsPlayerViewModel.playerSettingsState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SettingsPlayerView(
            state = state,
            onSettingsPlayerAction = { action ->
                when (action) {
                    is SettingsPlayerAction.NavigateBack -> {
                        navigator.pop()
                    }

                    else -> settingsPlayerViewModel.processAction(action)

                }
            }
        )
    }
}