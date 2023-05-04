/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:46
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.components.modifiers

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