/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.manager

import com.mvproject.tinyiptv.data.helpers.InfoChannelHelper
import com.mvproject.tinyiptv.data.helpers.PlaylistContentHelper
import com.mvproject.tinyiptv.data.helpers.SyncHelper
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
import io.github.aakira.napier.Napier

class PlaylistManager(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val playlistContentHelper: PlaylistContentHelper,
    private val infoChannelHelper: InfoChannelHelper,
    private val syncHelper: SyncHelper
) {

    val playlistCount
        get() = playlistsRepository.playlistCount

    val currentPlaylistId
        get() = preferenceRepository.currentPlaylistId

    fun loadPlaylists(): List<Playlist> {
        return playlistsRepository.getAllPlaylists()
    }

    val allPlaylistsFlow
        get() = playlistsRepository.allPlaylistsFlow()

    suspend fun setCurrentPlaylist(playlistId: Long) {
        preferenceRepository.setCurrentPlaylistId(playlistId)
    }

    suspend fun savePlaylist(playlist: Playlist) {
        playlistsRepository.insertPlaylist(playlist)

        savePlaylistData(playlist = playlist)

        checkPlaylistAsCurrent(playlist = playlist)

        infoChannelHelper.checkPlaylistChannelsInfo(playlist = playlist)

        if (playlist.isRemote) {
            syncHelper.scheduleChannelsUpdate(
                playlistId = playlist.id,
                updatePeriod = playlist.updatePeriod
            )
        }
    }

    suspend fun loadSelectedPlayList(): Playlist? {
        val current = preferenceRepository.loadCurrentPlaylistId()
        return playlistsRepository.getPlaylistById(current)
    }

    suspend fun loadPlaylistById(playlistId: Long): Playlist? {
        return playlistsRepository.getPlaylistById(id = playlistId)
    }

    suspend fun deletePlaylist(playlistId: Long) {
        playlistsRepository.deletePlaylistById(id = playlistId)
        playlistChannelsRepository.deleteAllChannelByListId(listId = playlistId)
        playlistChannelsRepository.deleteChannelFromFavoriteByList(listId = playlistId)
        syncHelper.cancelScheduleChannelsUpdate(playlistId)
    }

    suspend fun savePlaylistData(playlist: Playlist) {
        val channels = playlistContentHelper.getPlaylistData(playlist)
        playlistChannelsRepository.insertChannels(channels)
        playlistChannelsRepository.updateFavorites(channels)
        Napier.i("testing savePlaylistData inserted ${channels.count()}")

    }

    private suspend fun checkPlaylistAsCurrent(playlist: Playlist) {
        if (playlistCount == INT_VALUE_1) {
            Napier.w("testing need set as current")
            setCurrentPlaylist(playlist.id)
        }
    }
}