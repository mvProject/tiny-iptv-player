/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.manager

import com.mvproject.videoapp.data.enums.ChannelsViewType
import com.mvproject.videoapp.data.models.channels.ChannelsGroup
import com.mvproject.videoapp.data.models.channels.PlaylistChannel
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.data.repository.EpgProgramRepository
import com.mvproject.videoapp.data.repository.PlaylistChannelsRepository
import com.mvproject.videoapp.data.repository.PreferenceRepository
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier
import kotlin.random.Random

class PlaylistChannelManager(
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val epgProgramRepository: EpgProgramRepository
) {

    /*
        suspend fun getGroupOfChannel(channelId: Long): List<PlaylistChannel> {
            val selectedChannel = playlistChannelsRepository.getChannelById(channelId)

            return when (selectedChannel.channelGroup) {
                "All Channels" -> playlistChannelsRepository.getAllChannelsByListId(
                    selectedChannel.parentListId
                )

                else -> playlistChannelsRepository.getGroupedChannelsByListId(
                    selectedChannel.parentListId,
                    selectedChannel.channelGroup
                )
            }
        }
    */

    suspend fun getChannelsByGroup(group: String): List<PlaylistChannel> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()

        return when (group) {
            "All Channels" -> playlistChannelsRepository.getAllChannelsByListId(
                listId = currentPlaylistId
            )

            "Favorites" -> {
                val favoriteIds = playlistChannelsRepository
                    .getFavouriteChannelsIds(listId = currentPlaylistId)
                playlistChannelsRepository.getChannelsEntitiesByIds(
                    listId = currentPlaylistId,
                    ids = favoriteIds
                )
            }

            else -> playlistChannelsRepository.getGroupedChannelsByListId(
                listId = currentPlaylistId,
                group = group
            )
        }
    }

    fun getEpgForChannel(
        channelId: Long,
        actualTime: Long = actualDate,
        limit: Long = 1
    ): List<EpgProgram> {
        return epgProgramRepository
            .getEpgProgramsByIdWithLimit(channelId, limit, actualTime)
    }

    fun getEpgListForChannel(
        channelId: Long,
        actualTime: Long = actualDate
    ): List<EpgProgram> {
        return epgProgramRepository
            .getEpgProgramsById(channelId, actualTime)
    }

    suspend fun loadAllGroupsFromPlaylist(): List<ChannelsGroup> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
        val groups = playlistChannelsRepository.getAllChannelGroupsByListId(currentPlaylistId)
        val allChannelsCount =
            playlistChannelsRepository.getAllChannelCountByListId(currentPlaylistId).toInt()

        return buildList {
            add(
                ChannelsGroup(
                    groupName = "All Channels",
                    groupContentCount = allChannelsCount
                )
            )

            add(
                ChannelsGroup(
                    groupName = "Favorites",
                    groupContentCount = 0
                )
            )

            groups.forEach {
                val data =
                    playlistChannelsRepository.getAllChannelCountByListId(currentPlaylistId, it)
                add(
                    ChannelsGroup(
                        groupName = data.channelGroup,
                        groupContentCount = data.COUNT.toInt()
                    )
                )
            }
        }
    }

    suspend fun getFavoriteIds(): List<Long> {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
        return playlistChannelsRepository.getFavouriteChannelsIds(listId = currentPlaylistId)
    }

    suspend fun addChannelToFavorites(channelId: Long) {
        val id: Long = Random.nextLong()
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
        Napier.w("testing addChannelToFavorites id:$id, channelId:$channelId, currentPlaylistId:$currentPlaylistId")
        playlistChannelsRepository.addChannelToFavorite(
            id = id,
            channelId = channelId,
            listId = currentPlaylistId
        )

    }

    suspend fun deleteChannelFromFavorites(channelId: Long) {
        val currentPlaylistId = preferenceRepository.loadCurrentPlaylistId()
        playlistChannelsRepository.deleteChannelFromFavorite(
            channelId = channelId,
            listId = currentPlaylistId
        )
        Napier.w("testing deleteChannelFromFavorites channelId:$channelId, currentPlaylistId:$currentPlaylistId")

    }

    suspend fun setChannelsViewType(type: ChannelsViewType) {
        preferenceRepository.setChannelsViewType(type = type.name)
    }

    suspend fun getChannelsViewType(): ChannelsViewType {
        val currentType = preferenceRepository.getChannelsViewType()
        return if (currentType != null) {
            ChannelsViewType.valueOf(currentType)
        } else
            ChannelsViewType.LIST
    }
}




