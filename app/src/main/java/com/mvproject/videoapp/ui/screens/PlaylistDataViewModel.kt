/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.manager.PlaylistChannelManager
import com.mvproject.videoapp.data.manager.PlaylistManager
import com.mvproject.videoapp.data.models.channels.ChannelsGroup
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.utils.AppConstants.INT_NO_VALUE
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_1
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistDataViewModel(
    private val playlistManager: PlaylistManager,
    private val playlistChannelManager: PlaylistChannelManager
) : ViewModel() {

    private val _playlistDataState = MutableStateFlow(PlaylistDataState())
    val playlistDataState = _playlistDataState.asStateFlow()

    private val _playlistState = MutableStateFlow(PlaylistState())
    val playlistState = _playlistState.asStateFlow()

    private var currentPlaylists: List<Playlist> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistManager.allPlaylistsFlow.collect { playlists ->
                currentPlaylists = playlists

                val currentPlaylist = playlistManager.loadSelectedPlayList()
                Napier.w("testing allPlaylistsFlow")
                currentPlaylist?.let { id ->
                    val indexOfSelected = currentPlaylists.indexOf(id)

                    //  loadChannels()

                    _playlistState.update { current ->
                        current.copy(
                            isPlaylistSelectorVisible = currentPlaylists.count() > INT_VALUE_1,
                            playlists = currentPlaylists.map { it.listName },
                            playlistSelectedIndex = indexOfSelected
                        )
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            playlistManager.currentPlaylistId.collect { id ->
                Napier.w("testing flow currentPlaylistId $id")
                val currentPlaylist = playlistManager.loadSelectedPlayList()
                currentPlaylist?.let { currentId ->
                    val indexOfSelected = currentPlaylists.indexOf(currentId)

                    loadChannels()

                    _playlistState.update { current ->
                        current.copy(
                            playlistSelectedIndex = indexOfSelected
                        )
                    }
                }
            }
        }
    }

    private fun loadChannels() {
        Napier.w("testing loadChannels")
        viewModelScope.launch(Dispatchers.IO) {
            val groups = playlistChannelManager.loadAllGroupsFromPlaylist()
            _playlistDataState.update { current ->
                current.copy(groups = groups, isLoading = false)
            }
        }
    }

    fun changePlaylist(playlistIndex: Int) {
        val current = playlistState.value.playlistSelectedIndex
        if (current != playlistIndex) {
            _playlistDataState.update {
                it.copy(isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO) {
                val selected = currentPlaylists[playlistIndex]
                playlistManager.setCurrentPlaylist(playlistId = selected.id)
            }
        }
    }

    data class PlaylistDataState(
        val groups: List<ChannelsGroup> = emptyList(),
        val isLoading: Boolean = false,
    )

    data class PlaylistState(
        val playlistSelectedIndex: Int = INT_NO_VALUE,
        val isPlaylistSelectorVisible: Boolean = false,
        val playlists: List<String> = emptyList(),
    )


}