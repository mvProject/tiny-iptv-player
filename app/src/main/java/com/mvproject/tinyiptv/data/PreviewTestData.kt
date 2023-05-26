/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:41
 *
 */

package com.mvproject.tinyiptv.data

import com.mvproject.tinyiptv.data.models.channels.ChannelsGroup
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
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

    val testPlaylist =
        Playlist(
            id = Random.nextLong(),
            listName = "listName",
            listUrl = "listUrl",
            lastUpdateDate = Random.nextLong(),
            updatePeriod = 3
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
    val testPlaylists = buildList {
        repeat(2) {
            add(
                Playlist(
                    id = Random.nextLong(),
                    listName = "listName $it",
                    listUrl = "listUrl $it",
                    lastUpdateDate = Random.nextLong(),
                    updatePeriod = 3
                )
            )
        }
    }

    val testChannelsGroups = buildList {
        repeat(10) {
            add(
                ChannelsGroup(
                    groupName = "listName $it",
                    groupContentCount = it * (it + 1)
                )
            )
        }
    }
}