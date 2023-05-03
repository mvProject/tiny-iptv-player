/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp

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