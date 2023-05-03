/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.models.epg

import com.mvproject.videoapp.components.player.calculateProgramProgress
import com.mvproject.videoapp.utils.AppConstants

data class EpgProgram(
    val start: Long,
    val stop: Long,
    val channelId: Long,
    val title: String = AppConstants.EMPTY_STRING,
    val description: String = AppConstants.EMPTY_STRING
) {

    val key
        get() = (start + stop).toString() + title

    val programProgress
        get() = calculateProgramProgress(
            startTime = start,
            endTime = stop
        )

    override fun toString() =
        StringBuilder()
            .append("\n")
            .append(channelId)
            .append("\n")
            .append("$start - $stop")
            .append("\n")
            .append("title: $title")
            .append("\n")
            .append("description: $description")
            .toString()
}