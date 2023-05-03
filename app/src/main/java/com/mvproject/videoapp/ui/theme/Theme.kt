/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun VideoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appColors = if (darkTheme) {
        DarkColor
    } else {
        LightColor
    }

    CompositionLocalProvider(
        LocalColors provides appColors,
        LocalDimens provides Dimens(),
        LocalTypography provides AppTypography(),
    ) {
        MaterialTheme(
            colors = appColors.material,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}