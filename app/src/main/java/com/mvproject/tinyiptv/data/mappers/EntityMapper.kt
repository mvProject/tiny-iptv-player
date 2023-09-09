/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.mappers

import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgInfo
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.models.epg.SelectedEpg
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.utils.TimeUtils.correctTimeZone
import com.mvproject.tinyiptv.utils.TimeUtils.toBoolean
import com.mvproject.tinyiptv.utils.TimeUtils.toLong
import videoappdb.EpgInfoEntity
import videoappdb.EpgProgramEntity
import videoappdb.PlaylistChannelEntity
import videoappdb.PlaylistEntity
import videoappdb.SelectedEpgEntity

object EntityMapper {
    fun PlaylistChannelEntity.toPlaylistChannel() = with(this) {
        PlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toPlaylistChannelEntity() = with(this) {
        PlaylistChannelEntity(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            channelGroup = channelGroup,
            epgId = epgId,
            parentListId = parentListId
        )
    }

    fun PlaylistChannel.toTvPlaylistChannel(
        isFavorite: Boolean = false,
        isEpgUsing: Boolean = false
    ) = with(this) {
        TvPlaylistChannel(
            channelName = channelName,
            channelLogo = channelLogo,
            channelUrl = channelUrl,
            epgId = epgId,
            isInFavorites = isFavorite,
            isEpgUsing = isEpgUsing
        )
    }

    fun PlaylistEntity.toPlaylist() = with(this) {
        Playlist(
            id = id,
            playlistTitle = title,
            playlistUrl = url,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            playlistLocalName = localFilename,
            isLocalSource = isLocal.toBoolean()
        )
    }

    fun Playlist.toPlaylistEntity() = with(this) {
        PlaylistEntity(
            id = id,
            title = playlistTitle,
            url = playlistUrl,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod,
            localFilename = playlistLocalName,
            isLocal = isLocalSource.toLong()
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

    fun EpgInfoEntity.toEpgInfo() = with(this) {
        EpgInfo(
            channelId = channelId,
            channelName = channelName,
            channelLogo = channelLogo
        )
    }

    fun SelectedEpgEntity.toSelectedEpg() = with(this) {
        SelectedEpg(
            channelName = channelName,
            channelEpgId = channelEpgId
        )
    }
}



