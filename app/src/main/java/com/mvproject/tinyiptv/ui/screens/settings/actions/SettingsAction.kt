/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 12:50
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.actions

sealed class SettingsAction {
    object NavigatePlaylistSettings : SettingsAction()
    object NavigateEpgSettings : SettingsAction()
    object NavigatePlayerSettings : SettingsAction()
    object NavigateAppSettings : SettingsAction()
    object NavigateBack : SettingsAction()
}

