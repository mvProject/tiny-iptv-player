/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
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