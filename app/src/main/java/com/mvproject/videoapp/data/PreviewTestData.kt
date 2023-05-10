/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:41
 *
 */

package com.mvproject.videoapp.data

import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import kotlin.random.Random

object PreviewTestData {
    val testProgram =
        PlaylistChannelWithEpg(
            id = Random.nextLong(),
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
            channelEpg = emptyList()
        )
}