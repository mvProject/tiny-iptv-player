/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.tinyiptv.data.helpers

import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier

class InfoChannelHelper(
    private val preferenceRepository: PreferenceRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val playlistChannelsRepository: PlaylistChannelsRepository
) {
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

    suspend fun checkAllPlaylistsChannelsInfo() {
        val allPlaylists = playlistsRepository.getAllPlaylists().map { it.id }
        checkAllPlaylistMainInfo(playlistIds = allPlaylists)
        checkAllPlaylistAlterInfo(playlistIds = allPlaylists)

        preferenceRepository.setEpgInfoLastUpdate(timestamp = actualDate)
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