/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 11:28
 *
 */

package com.mvproject.videoapp.ui.screens.playlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.manager.PlaylistChannelManager
import com.mvproject.videoapp.data.manager.PlaylistManager
import com.mvproject.videoapp.data.models.channels.ChannelsGroup
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.utils.AppConstants
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

    private var currentPlaylists: List<Playlist> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistManager.allPlaylistsFlow.collect { playlists ->
                currentPlaylists = playlists

                val currentPlaylist = playlistManager.loadSelectedPlayList()
                currentPlaylist?.let { id ->
                    val indexOfSelected = currentPlaylists.indexOf(id)

                    _playlistDataState.update { current ->
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
                if (id != AppConstants.LONG_NO_VALUE) {
                    val currentPlaylist = playlistManager.loadSelectedPlayList()
                    currentPlaylist?.let { currentId ->
                        val indexOfSelected = currentPlaylists.indexOf(currentId)
                        _playlistDataState.update { current ->
                            current.copy(
                                playlistSelectedIndex = indexOfSelected,
                                isLoading = true
                            )
                        }

                        loadChannels()
                    }
                } else {
                    Napier.e("currentPlaylistId LONG_NO_VALUE")
                    _playlistDataState.update { current ->
                        current.copy(groups = emptyList(), isLoading = false)
                    }
                }
            }
        }
    }

    private fun loadChannels() {
        viewModelScope.launch(Dispatchers.IO) {
            val groups = playlistChannelManager.loadAllGroupsFromPlaylist()
            _playlistDataState.update { current ->
                current.copy(groups = groups, isLoading = false, isDataLoaded = true)
            }
        }
    }

    fun changePlaylist(playlistIndex: Int) {
        val current = playlistDataState.value.playlistSelectedIndex
        if (current != playlistIndex) {
            viewModelScope.launch(Dispatchers.IO) {
                val selected = currentPlaylists[playlistIndex]
                playlistManager.setCurrentPlaylist(playlistId = selected.id)
            }
        }
    }

    data class PlaylistDataState(
        val groups: List<ChannelsGroup> = emptyList(),
        val playlists: List<String> = emptyList(),
        val isLoading: Boolean = false,
        val isDataLoaded: Boolean = false,
        val playlistSelectedIndex: Int = INT_NO_VALUE,
        val isPlaylistSelectorVisible: Boolean = false
    ) {
        val dataIsEmpty
            get() = isDataLoaded && groups.isEmpty()
    }
}