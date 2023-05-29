/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.repository

import com.mvproject.tinyiptv.VideoAppDatabase
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toChannelEntity
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylistChannel
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import videoappdb.FavoriteChannelEntity
import videoappdb.GetGroupedChannelCountByListId
import kotlin.random.Random

class PlaylistChannelsRepository(private val db: VideoAppDatabase) {
    private val queries = db.playlistChannelQueries

    fun getAllChannelsByListId(listId: Long): List<PlaylistChannel> {
        return queries.getAllChannelEntitiesByListId(listId).executeAsList().map {
            it.toPlaylistChannel()
        }
    }


    fun getAllChannelGroupsByListId(listId: Long): List<String> {
        return queries.getAllChannelGroupsByListId(listId).executeAsList().toSet().toList()
    }

    fun getAllChannelCountByListId(listId: Long): Long {
        return queries.getAllChannelCountByListId(listId).executeAsOne()
    }

    fun getAllChannelCountByListId(listId: Long, group: String): GetGroupedChannelCountByListId {
        return queries.getGroupedChannelCountByListId(listId, group).executeAsOne()
    }

    fun getChannelsWithMainInfo(listId: Long): List<PlaylistChannel> {
        return queries.getChannelsWithMainInfo(listId).executeAsList().map {
            it.toPlaylistChannel()
        }
    }

    fun getChannelsWithAlterInfo(listId: Long): List<PlaylistChannel> {
        return queries.getChannelsWithAlterInfo(listId).executeAsList().map {
            it.toPlaylistChannel()
        }
    }


    fun getGroupedChannelsByListId(listId: Long, group: String): List<PlaylistChannel> {
        return queries.getGroupedChannelEntitiesByListId(listId, group).executeAsList().map {
            it.toPlaylistChannel()
        }
    }

    fun getChannelsEntitiesByIds(listId: Long, ids: List<Long>): List<PlaylistChannel> {
        return queries.getChannelsEntitiesByIds(listId, ids).executeAsList().map {
            it.toPlaylistChannel()
        }
    }

    fun getFavouriteChannelsIds(listId: Long): List<Long> {
        return queries.getFavoriteChannelsByListId(listId).executeAsList()
    }

    fun getFavouriteChannelsNames(listId: Long): List<String> {
        return queries.getFavoriteChannelsNames(listId).executeAsList()
    }

    fun getFavouriteChannels(listId: Long): List<FavoriteChannelEntity> {
        return queries.getFavoriteChannels(listId).executeAsList()
    }

    suspend fun addChannelToFavorite(id: Long, channelId: Long, channelName: String, listId: Long) {
        withContext(Dispatchers.IO) {
            val favs = queries.getFavoriteChannelsByListId(listId).executeAsList().count()
            val order = (favs + INT_VALUE_1).toLong()
            queries.insertFavoriteChannelEntity(
                FavoriteChannelEntity(
                    id = id,
                    channelId = channelId,
                    channelName = channelName,
                    channelOrder = order,
                    parentListId = listId
                )
            )
        }
    }

    suspend fun deleteChannelFromFavorite(channelId: Long, listId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteFavoriteChannelByChannelId(
                id = listId, channelId = channelId
            )
        }
    }

    suspend fun deleteChannelFromFavoriteByList(listId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteFavoriteChannels(
                id = listId
            )
        }
    }

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

    suspend fun updateFavorites(channels: List<PlaylistChannel>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                val parentListId = channels.map { it.parentListId }.toSet().first()
                val favChannels = getFavouriteChannels(parentListId).sortedBy { it.channelOrder }
                val channelNames = channels.map { it.channelName }

                queries.deleteFavoriteChannels(parentListId)

                favChannels.forEachIndexed { index, favChn ->
                    if (favChn.channelName in channelNames) {
                        Napier.w("testing updateFavorites favs add to favs:${favChn.channelName}")
                        val channelId =
                            channels.firstOrNull { it.channelName == favChn.channelName }?.id
                        if (channelId != null) {
                            queries.insertFavoriteChannelEntity(
                                FavoriteChannelEntity(
                                    id = Random.nextLong(),
                                    channelId = channelId,
                                    channelName = favChn.channelName,
                                    channelOrder = index.toLong(),
                                    parentListId = parentListId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}