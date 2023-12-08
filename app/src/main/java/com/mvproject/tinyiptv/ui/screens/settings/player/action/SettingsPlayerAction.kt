/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:15
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.player.action

sealed class SettingsPlayerAction {
    data class SetResizeMode(val mode: Int) : SettingsPlayerAction()
    data class SetRatioMode(val mode: Int) : SettingsPlayerAction()
    data class SetFullScreenMode(val state: Boolean) : SettingsPlayerAction()
}

