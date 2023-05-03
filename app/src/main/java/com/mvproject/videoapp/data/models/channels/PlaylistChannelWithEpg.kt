/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.models.channels

import com.mvproject.videoapp.data.models.epg.EpgProgram

data class PlaylistChannelWithEpg(
    val id: Long,
    val channelName: String,
    val channelUrl: String,
    val channelLogo: String,
    val epgId: Long? = null,
    val isInFavorites: Boolean = false,
    val channelEpg: List<EpgProgram> = emptyList()
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("id: $id")
            .append("\n")
            .append("channelName: $channelName")
            .append("\n")
            .append("channelUrl: $channelUrl")
            .append("\n")
            .append("channelLogo: $channelLogo")
            .append("\n")
            .append("channelEpgCount: ${channelEpg.count()}")
            .toString()
}