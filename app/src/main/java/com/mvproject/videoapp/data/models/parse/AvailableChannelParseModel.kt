/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.models.parse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableChannelParseModel(
    @SerialName("chan_id")
    val channelId: String,
    @SerialName("chan_names")
    val channelNames: String,
    @SerialName("chan_icon")
    val channelIcon: String
)