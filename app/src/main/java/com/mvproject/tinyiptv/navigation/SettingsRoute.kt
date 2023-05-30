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
import com.mvproject.tinyiptv.ui.screens.settings.actions.SettingsAction
import com.mvproject.tinyiptv.ui.screens.settings.view.SettingsView

class SettingsRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SettingsView(
            onSettingsAction = { action ->
                when (action) {
                    is SettingsAction.NavigateBack -> navigator.pop()
                    is SettingsAction.NavigatePlaylistSettings -> navigator.push(
                        SettingsPlaylistRoute
                    )

                    is SettingsAction.NavigateEpgSettings -> navigator.push(
                        SettingsEpgRoute
                    )

                    is SettingsAction.NavigatePlayerSettings -> navigator.push(
                        SettingsPlayerRoute
                    )

                    is SettingsAction.NavigateAppSettings -> {}
                }
            }
        )
    }
}