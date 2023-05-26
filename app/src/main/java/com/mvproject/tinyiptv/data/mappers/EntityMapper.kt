/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.mappers

import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.tinyiptv.data.models.epg.ChannelInfoAlter
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.utils.TimeUtils.correctTimeZone
import com.mvproject.tinyiptv.utils.TimeUtils.toBoolean
import com.mvproject.tinyiptv.utils.TimeUtils.toLong
import videoappdb.ChannelInfoAlterEntity
import videoappdb.EpgProgramEntity
import videoappdb.GetChannelsWithAlterInfo
import videoappdb.GetChannelsWithMainInfo
import videoappdb.PlaylistChannelEntity
import videoappdb.PlaylistEntity

object EntityMapper {
    fun GetChannelsWithMainInfo.toPlaylistChannel() = with(this) {
        PlaylistChannel(
            id = channelId,
            channelName = channelName,
            channelLogo = channelInfoLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = channelInfoId,
            parentListId = parentListId
        )
    }

    fun GetChannelsWithAlterInfo.toPlaylistChannel() = with(this) {
        PlaylistChannel(
            id = channelId,
            channelName = channelName,
            channelLogo = channelInfoLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgAlterId = channelInfoId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannelEntity.toPlaylistChannel() = with(this) {
        PlaylistChannel(
            id = id,
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            epgAlterId = epgAlterId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toChannelEntity() = with(this) {
        PlaylistChannelEntity(
            id = id,
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            epgAlterId = epgAlterId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toPlaylistChannelWithEpg() = with(this) {
        val epgIds = listOf(epgId, epgAlterId).firstNotNullOfOrNull { it }
        PlaylistChannelWithEpg(
            id = id,
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            epgId = epgIds
        )
    }

    fun PlaylistEntity.toPlaylist() = with(this) {
        Playlist(
            id = id,
            listName = listName,
            listUrl = listUrl,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            isMainInfoUse = isMainInfoUse.toBoolean(),
            isAlterInfoUse = isAlterInfoUse.toBoolean(),
            isRemoteSource = isRemote.toBoolean()
        )
    }

    fun Playlist.toPlaylistEntity() = with(this) {
        PlaylistEntity(
            id = id,
            listName = listName,
            listUrl = listUrl,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            isMainInfoUse = isMainInfoUse.toLong(),
            isAlterInfoUse = isAlterInfoUse.toLong(),
            isRemote = isRemoteSource.toLong()
        )
    }

    fun EpgProgramEntity.toEpgProgram() = with(this) {
        EpgProgram(
            channelId = channelId,
            start = programStart,
            stop = programEnd,
            title = title,
            description = description
        )
    }

    fun EpgProgram.toEpgProgramEntity() = with(this) {
        EpgProgramEntity(
            channelId = channelId,
            programStart = start.correctTimeZone(),
            programEnd = stop.correctTimeZone(),
            title = title,
            description = description
        )
    }

    fun ChannelInfoAlterEntity.toChannelInfoAlter() = with(this) {
        ChannelInfoAlter(
            channelId = channelId,
            channelAlterId = channelInfoId
        )
    }
}



