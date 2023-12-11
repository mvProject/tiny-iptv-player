/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 14:45
 *
 */

package com.mvproject.tinyiptv.ui.components.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun OverlayContent(
    isVisible: Boolean = false,
    onViewTap: () -> Unit = {},
    contentAlpha: Float = MaterialTheme.dimens.alpha80,
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onViewTap() }
                    )
                },
            contentAlignment = Alignment.Center
        ) {

            content()
        }
    }
}