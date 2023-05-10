/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 14:53
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.ui.components.modifiers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.mvproject.videoapp.data.enums.player.PlayerUICommands

fun Modifier.defaultPlayerTapGestures(
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) = pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = {
            onPlayerUICommand(PlayerUICommands.TOGGLE_FULL_SCREEN)
        },
        onTap = {
            onPlayerUICommand(PlayerUICommands.TOGGLE_UI_VISIBILITY)
        },
        onLongPress = {
            onPlayerUICommand(PlayerUICommands.TOGGLE_EPG_VISIBILITY)
        }
    )
}