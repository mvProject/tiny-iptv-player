/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.repository

import com.mvproject.videoapp.VideoAppDatabase
import com.mvproject.videoapp.data.mappers.EntityMapper.toPlaylist
import com.mvproject.videoapp.data.mappers.EntityMapper.toPlaylistEntity
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistsRepository(private val db: VideoAppDatabase) {
    private val queries = db.playlistQueries

    suspend fun getPlaylistById(id: Long): Playlist? {
        return withContext(Dispatchers.IO) {
            queries.getPlaylistEntityById(id).executeAsOneOrNull()
        }?.toPlaylist()
    }

    fun allPlaylistsFlow(): Flow<List<Playlist>> {
        return queries.getAllPlaylistEntities()
            .asFlow()
            .mapToList()
            .map { list ->
                list.map {
                    it.toPlaylist()
                }
            }
    }

    fun getAllPlaylists(): List<Playlist> {
        return queries.getAllPlaylistEntities().executeAsList().map {
            it.toPlaylist()
        }
    }

    val playlistCount
        get() = queries.getAllPlaylistEntities().executeAsList().count()


    suspend fun deletePlaylistById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deletePlaylistEntityById(id)
        }
    }

    suspend fun insertPlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            queries.insertPlaylistEntity(
                playlist.toPlaylistEntity()
            )
        }
    }
}
