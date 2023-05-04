/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:46
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.components.modifiers

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.ViewSettingsRequest
import kotlin.math.abs

fun Modifier.defaultPlayerVerticalGestures(
    onAction: (request: ViewSettingsRequest) -> Unit
) =
    pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragEnd = {
                onAction(ViewSettingsRequest.NONE)
            },
            onVerticalDrag = { change, dragAmount ->
                val request = when {
                    change.position.x < size.width * 0.3 -> {
                        if (dragAmount > 0) {
                            ViewSettingsRequest.BRIGHTNESS_DOWN
                        } else {
                            ViewSettingsRequest.BRIGHTNESS_UP
                        }
                    }

                    change.position.x > size.width * 0.7 -> {
                        if (dragAmount > 0) {
                            ViewSettingsRequest.VOLUME_DOWN
                        } else {
                            ViewSettingsRequest.VOLUME_UP
                        }
                    }

                    else -> {
                        ViewSettingsRequest.NONE
                    }
                }
                onAction(request)
                if (change.positionChange() != Offset.Zero) change.consume()
            }
        )
    }

fun Modifier.defaultPlayerHorizontalGestures(
    onPlayerCommand: (command: PlayerCommands) -> Unit
) = pointerInput(Unit) {
    var startX = -1f
    var endX = -1f

    detectHorizontalDragGestures(
        onDragStart = {
            startX = it.x
        },
        onDragEnd = {
            val moveOffset = endX - startX
            if (abs(moveOffset) > (size.width * 0.25)) {
                val directionRequestType = if (moveOffset > 0) {
                    PlayerCommands.NEXT_VIDEO
                } else PlayerCommands.PREVIOUS_VIDEO
                onPlayerCommand(directionRequestType)

            }
        },
        onHorizontalDrag = { change, _ ->
            endX = change.position.x
        }
    )
}
