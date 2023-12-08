/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 12:42
 *
 */

package com.mvproject.tinyiptv.data.enums

import androidx.annotation.StringRes
import com.mvproject.tinyiptv.R

enum class RatioMode(val value: Int, @StringRes val title: Int, val ratio: Float) {
    Original(0, R.string.video_ratio_mode_original, 1f),
    WideScreen(1, R.string.video_ratio_mode_wideScreen, 1.777f),
    FullScreen(2, R.string.video_ratio_mode_fullScreen, 1.333f),
    Cinematic(3, R.string.video_ratio_mode_cinematic, 2.333f),
    Square(4, R.string.video_ratio_mode_square, 1f);

    companion object {
        fun toggleRatioMode(current: RatioMode) =
            when (current) {
                Original -> WideScreen
                WideScreen -> FullScreen
                FullScreen -> Cinematic
                Cinematic -> Square
                Square -> Original
            }
    }
}