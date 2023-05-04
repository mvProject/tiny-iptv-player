/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.models.channels

data class ChannelsGroup(
    val groupName: String,
    val groupContentCount: Int
) {
    override fun toString() =
        StringBuilder()
            .append("\n")
            .append("groupName: $groupName")
            .append("\n")
            .append("groupContentCount: $groupContentCount")
            .toString()
}