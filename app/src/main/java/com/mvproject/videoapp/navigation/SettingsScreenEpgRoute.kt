/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.ui.screens.settings.view.SettingsEpgView
import com.mvproject.videoapp.ui.screens.settings.viewmodel.EpgSettingsViewModel
import org.koin.androidx.compose.koinViewModel

class SettingsScreenEpgRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val epgSettingsViewModel: EpgSettingsViewModel = koinViewModel()

        SettingsEpgView(viewModel = epgSettingsViewModel)
    }
}