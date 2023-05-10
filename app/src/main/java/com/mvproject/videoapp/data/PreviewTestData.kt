/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
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