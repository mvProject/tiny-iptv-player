/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 12:50
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.actions

sealed class SettingsPlayerAction {
    data class ChangeResizeModePeriod(val mode: Int) : SettingsPlayerAction()
    data class ChangeFullScreenMode(val state: Boolean) : SettingsPlayerAction()
    object NavigateBack : SettingsPlayerAction()
}

