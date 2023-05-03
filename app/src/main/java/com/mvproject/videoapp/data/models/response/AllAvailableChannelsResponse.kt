/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.models.response

import com.mvproject.videoapp.data.models.parse.AvailableChannelParseModel
import kotlinx.serialization.Serializable

@Serializable
data class AllAvailableChannelsResponse(
    val channels: List<AvailableChannelParseModel>
)