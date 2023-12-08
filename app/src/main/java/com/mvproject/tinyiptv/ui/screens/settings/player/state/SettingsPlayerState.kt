/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:15
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.player.state

import com.mvproject.tinyiptv.data.enums.RatioMode
import com.mvproject.tinyiptv.data.enums.ResizeMode

data class SettingsPlayerState(
    val resizeMode: Int = ResizeMode.Fill.value,
    val ratioMode: Int = RatioMode.WideScreen.value,
    val isFullscreenEnabled: Boolean = true,
)