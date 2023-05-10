/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.models.channels

import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING
import kotlin.random.Random

data class PlaylistChannel(
    val id: Long = Random.nextLong(),
    val channelName: String,
    val channelLogo: String = EMPTY_STRING,
    val channelUrl: String,
    val channelGroup: String,
    val epgId: Long? = null,
    val epgAlterId: Long? = null,
    val parentListId: Long
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("id: $id")
            .append("\n")
            .append("channelName: $channelName")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelGroup: $channelGroup")
            .append("\n")
            .append("epgId: $epgId")
            .append("\n")
            .append("epgAlterId: $epgAlterId")
            .append("\n")
            .append("parentListId: $parentListId")
            .toString()
}