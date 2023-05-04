/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:34
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.helpers

import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.data.repository.PlaylistChannelsRepository
import com.mvproject.videoapp.data.repository.PlaylistsRepository
import com.mvproject.videoapp.data.repository.PreferenceRepository
import io.github.aakira.napier.Napier

class InfoChannelHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository
) {
    //todo perform after playlist
    suspend fun checkPlaylistChannelsInfo(playlist: Playlist) {
        Napier.i("testing isMainInfoUse:${playlist.isMainInfoUse}, isAlterInfoUse:${playlist.isAlterInfoUse}")
        if (playlist.isMainInfoUse) {
            checkPlaylistMainInfo(playlistId = playlist.id)
        }
        if (playlist.isAlterInfoUse) {
            checkPlaylistAlterInfo(playlistId = playlist.id)
        }
        Napier.i("testing checkPlaylistChannelsInfo complete")
    }

    //todo perform after epg info add
    suspend fun checkAllPlaylistsChannelsInfo() {
        val allPlaylists = playlistsRepository.getAllPlaylists().map { it.id }
        checkAllPlaylistMainInfo(playlistIds = allPlaylists)
        checkAllPlaylistAlterInfo(playlistIds = allPlaylists)
        Napier.i("testing checkAllPlaylistsChannelsInfo complete")
    }

    private suspend fun checkPlaylistMainInfo(playlistId: Long) {
        if (preferenceRepository.getMainInfoExist()) {
            Napier.w("testing checkPlaylistMainInfo isMainInfoExist need update")
            val channelsWithMainInfo =
                playlistChannelsRepository.getChannelsWithMainInfo(playlistId)
            playlistChannelsRepository.insertChannels(channelsWithMainInfo)
            Napier.i("testing checkPlaylistMainInfo for $playlistId complete, count:${channelsWithMainInfo.count()}")
        } else Napier.e("testing checkPlaylistMainInfo MainInfo not exist")
    }

    private suspend fun checkAllPlaylistMainInfo(playlistIds: List<Long>) {
        if (preferenceRepository.getMainInfoExist()) {
            playlistIds.forEach { id ->
                Napier.w("testing checkAllPlaylistMainInfo isMainInfoExist need update")
                val channelsWithMainInfo = playlistChannelsRepository.getChannelsWithMainInfo(id)
                playlistChannelsRepository.insertChannels(channelsWithMainInfo)
                Napier.i("testing checkPlaylistMainInfo for $id complete, count:${channelsWithMainInfo.count()}")
            }
        } else Napier.e("testing checkAllPlaylistMainInfo MainInfo not exist")
    }

    private suspend fun checkPlaylistAlterInfo(playlistId: Long) {
        if (preferenceRepository.getAlterInfoExist()) {
            Napier.w("testing checkPlaylistAlterInfo isAlterInfoExist need update")
            val channelsWithAlterInfo =
                playlistChannelsRepository.getChannelsWithAlterInfo(playlistId)
            playlistChannelsRepository.insertChannels(channelsWithAlterInfo)
            Napier.i("testing checkPlaylistAlterInfo for $playlistId complete, count:${channelsWithAlterInfo.count()}")
        } else Napier.e("testing checkPlaylistAlterInfo AlterInfo not exist")
    }

    private suspend fun checkAllPlaylistAlterInfo(playlistIds: List<Long>) {
        if (preferenceRepository.getAlterInfoExist()) {
            playlistIds.forEach { id ->
                Napier.w("testing checkAllPlaylistAlterInfo isAlterInfoExist need update")
                val channelsWithAlterInfo = playlistChannelsRepository.getChannelsWithAlterInfo(id)
                playlistChannelsRepository.insertChannels(channelsWithAlterInfo)
                Napier.i("testing checkPlaylistAlterInfo for $id complete, count:${channelsWithAlterInfo.count()}")
            }
        } else Napier.e("testing checkAllPlaylistAlterInfo AlterInfo not exist")
    }
}