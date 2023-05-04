/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:34
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.enums

@JvmInline
value class ResizeMode private constructor(val value: Int) {
    companion object {
        val Fit = ResizeMode(0)
        val FixedWidth = ResizeMode(1)
        val FixedHeight = ResizeMode(2)
        val Fill = ResizeMode(3)
        val Zoom = ResizeMode(4)

        fun toggleResizeMode(current: ResizeMode) =
            when (current) {
                Fit -> {
                    Fill
                }

                Fill -> {
                    Zoom
                }

                Zoom -> {
                    FixedHeight
                }

                FixedHeight -> {
                    FixedWidth
                }

                FixedWidth -> {
                    Fit
                }

                else -> {
                    Fit
                }
            }
    }
}