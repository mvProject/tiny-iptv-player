/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.ui.screens.settings.view.SettingsEpgView
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsEpgViewModel
import org.koin.androidx.compose.koinViewModel

class SettingsEpgRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val settingsEpgViewModel: SettingsEpgViewModel = koinViewModel()
        val updateState by settingsEpgViewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        SettingsEpgView(
            updateState = updateState,
            onUpdateTypeAction = settingsEpgViewModel::processAction,
            onBackNavigate = { navigator.pop() }
        )
    }
}