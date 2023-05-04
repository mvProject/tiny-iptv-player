/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.models.epg

data class ChannelInfoAlter(
    val channelId: String,
    val channelAlterId: Long,
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("channelId: $channelId")
            .append("\n")
            .append("channelAlterId: $channelAlterId")
            .toString()
}