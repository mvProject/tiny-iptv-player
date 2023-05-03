/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
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