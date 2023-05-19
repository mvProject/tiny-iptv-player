/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:41
 *
 */

package com.mvproject.videoapp.data

import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes

object PreviewTestData {
    val testProgram =
        PlaylistChannelWithEpg(
            id = Random.nextLong(),
            channelName = "channelName",
            channelLogo = "",
            channelUrl = "",
            channelEpg = emptyList()
        )

    val testEpgProgram =
        EpgProgram(
            channelId = Random.nextLong(),
            start = actualDate - 30.minutes.inWholeMilliseconds,
            stop = actualDate + 90.minutes.inWholeMilliseconds,
            title = "test title",
            description = "test description"
        )

    val testEpgPrograms = buildList {
        repeat(10) {
            add(
                EpgProgram(
                    channelId = Random.nextLong(),
                    start = actualDate + it * 30.minutes.inWholeMilliseconds,
                    stop = actualDate + (it + 1) * 30.minutes.inWholeMilliseconds,
                    title = "title $it",
                    description = "description $it"
                )
            )
        }
    }
}