/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 14:53
 *
 */

package com.mvproject.tinyiptv.utils

import androidx.compose.ui.unit.IntSize

object SizeUtils {
    val IntSize.screenMiddlePart
        get() =
            (this.width * AppConstants.SCREEN_PERCENTAGE_30).toInt()..(this.width * AppConstants.SCREEN_PERCENTAGE_70).toInt()

    val IntSize.screenLeftPart
        get() =
            AppConstants.INT_VALUE_ZERO..(this.width * AppConstants.SCREEN_PERCENTAGE_30).toInt()

    val IntSize.screenRightPart
        get() =
            (this.width * AppConstants.SCREEN_PERCENTAGE_70).toInt()..this.width
}