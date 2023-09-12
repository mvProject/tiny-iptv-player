/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 03.09.23, 21:48
 *
 */

package com.mvproject.tinyiptv.data.usecases

import com.mvproject.tinyiptv.data.datasource.RemotePlaylistDataSource
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.data.repository.FavoriteChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistChannelsRepository
import com.mvproject.tinyiptv.data.repository.PlaylistsRepository
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.TimeUtils
import io.github.aakira.napier.Napier

class UpdateRemotePlaylistChannelsUseCase(
    private val preferenceRepository: PreferenceRepository,
    private val remotePlaylistDataSource: RemotePlaylistDataSource,
    private val playlistChannelsRepository: PlaylistChannelsRepository,
    private val favoriteChannelsRepository: FavoriteChannelsRepository,
    private val playlistsRepository: PlaylistsRepository
) {
    suspend operator fun invoke(
        playlist: Playlist
    ) {
        Napier.w("testing update channels for playlist:${playlist.playlistTitle}")

        val channels = remotePlaylistDataSource.getFromRemotePlaylist(
            playlistId = playlist.id,
            url = playlist.playlistUrl
        )
        val favorites = favoriteChannelsRepository
            .loadPlaylistFavoriteChannelUrls(listId = playlist.id)

        playlistChannelsRepository.updatePlaylistChannels(channels)

        channels.forEach { channel ->
            if (channel.channelUrl in favorites) {
                Napier.w("testing update in favorite ${channel.channelName}")
                favoriteChannelsRepository.updatePlaylistFavoriteChannels(channel = channel)
            }
        }

        playlistsRepository.updatePlaylist(
            playlist = playlist.copy(lastUpdateDate = TimeUtils.actualDate)
        )

        preferenceRepository.setChannelsEpgInfoUpdateRequired(state = true)

        Napier.w("testing update channels finished")
    }
}