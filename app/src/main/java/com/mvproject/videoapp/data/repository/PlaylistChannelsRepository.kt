/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.repository

import com.mvproject.videoapp.VideoAppDatabase
import com.mvproject.videoapp.data.mappers.EntityMapper.toChannelEntity
import com.mvproject.videoapp.data.mappers.EntityMapper.toPlaylistChannel
import com.mvproject.videoapp.data.models.channels.PlaylistChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import videoappdb.FavoriteChannelEntity
import videoappdb.GetGroupedChannelCountByListId

class PlaylistChannelsRepository(private val db: VideoAppDatabase) {
    private val queries = db.playlistChannelQueries


    /*    suspend fun getChannelById(id: Long): PlaylistChannel {
            return withContext(Dispatchers.IO) {
                queries.getChannelEntityById(id)
                    .executeAsOne()
                    .toPlaylistChannel()
            }
        }*/

    fun getAllChannelsByListId(listId: Long): List<PlaylistChannel> {
        return queries.getAllChannelEntitiesByListId(listId)
            .executeAsList()
            .map {
                it.toPlaylistChannel()
            }
    }


    fun getAllChannelGroupsByListId(listId: Long): List<String> {
        return queries.getAllChannelGroupsByListId(listId)
            .executeAsList()
            .toSet()
            .toList()
    }

    fun getAllChannelCountByListId(listId: Long): Long {
        return queries.getAllChannelCountByListId(listId)
            .executeAsOne()
    }

    fun getAllChannelCountByListId(listId: Long, group: String): GetGroupedChannelCountByListId {
        return queries.getGroupedChannelCountByListId(listId, group)
            .executeAsOne()
    }

    fun getChannelsWithMainInfo(listId: Long): List<PlaylistChannel> {
        return queries.getChannelsWithMainInfo(listId)
            .executeAsList()
            .map {
                it.toPlaylistChannel()
            }
    }

    fun getChannelsWithAlterInfo(listId: Long): List<PlaylistChannel> {
        return queries.getChannelsWithAlterInfo(listId)
            .executeAsList()
            .map {
                it.toPlaylistChannel()
            }
    }


    fun getGroupedChannelsByListId(listId: Long, group: String): List<PlaylistChannel> {
        return queries.getGroupedChannelEntitiesByListId(listId, group)
            .executeAsList()
            .map {
                it.toPlaylistChannel()
            }
    }

    fun getChannelsEntitiesByIds(listId: Long, ids: List<Long>): List<PlaylistChannel> {
        return queries.getChannelsEntitiesByIds(listId, ids)
            .executeAsList()
            .map {
                it.toPlaylistChannel()
            }
    }

    fun getFavouriteChannelsIds(listId: Long): List<Long> {
        return queries.getFavoriteChannelsByListId(listId).executeAsList()
    }

    suspend fun addChannelToFavorite(id: Long, channelId: Long, listId: Long) {
        withContext(Dispatchers.IO) {
            queries.insertFavoriteChannelEntity(
                FavoriteChannelEntity(
                    id = id,
                    channelId = channelId,
                    parentListId = listId
                )
            )
        }
    }

    suspend fun deleteChannelFromFavorite(channelId: Long, listId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteFavoriteChannelListId(
                id = listId,
                channelId = channelId
            )
        }
    }

    /*

         fun getAllChannelsWithPrg(listId: Long): List<GetChannelsWithPrg> {
            return queries.getChannelsWithPrg(listId).executeAsList()
        }

        fun getGroupChannelsWithPrg(listId: Long, group: String): List<GetChannelsGroupWithPrg> {
            return queries.getChannelsGroupWithPrg(group, listId).executeAsList()
        }

     fun getChannelsByPlaylistPaged(
            playlistId: Long,
            limit: Long,
            offset: Long
        ): List<PlaylistChannel> {
            return queries.getChannelsByPlaylistPaged(playlistId, limit, offset)
                .executeAsList()
                .map {
                    it.toPlaylistChannel()
                }
        }

        fun getChannelsByGroupPaged(
            playlistId: Long,
            groupName: String,
            limit: Long,
            offset: Long
        ): List<PlaylistChannel> {
            return queries.getChannelsByGroupPaged(playlistId, groupName, limit, offset)
                .executeAsList()
                .map {
                    it.toPlaylistChannel()
                }
        }

        suspend fun deleteChannelById(id: Long) {
            withContext(Dispatchers.IO) {
                queries.transaction {
                    queries.deleteChannelEntityById(id)
                }
            }
        }
        */


    suspend fun deleteAllChannelByListId(listId: Long) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.deleteChannelEntityByListId(listId)
            }
        }
    }

    suspend fun insertChannels(channels: List<PlaylistChannel>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                channels.forEach { item ->
                    queries.insertChannelEntity(
                        item.toChannelEntity()
                    )
                }
            }
        }
    }
}