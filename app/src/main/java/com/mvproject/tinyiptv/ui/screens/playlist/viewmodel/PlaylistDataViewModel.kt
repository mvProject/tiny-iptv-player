/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 11:28
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.manager.PlaylistChannelManager
import com.mvproject.tinyiptv.data.manager.PlaylistManager
import com.mvproject.tinyiptv.data.models.channels.ChannelsGroup
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.utils.AppConstants
import com.mvproject.tinyiptv.utils.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
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

    fun dataInit() {
        _playlistDataState.update { current ->
            current.copy(isLoading = true)
        }

        viewModelScope.launch {
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

        viewModelScope.launch {
            playlistManager.currentPlaylistId.collect { id ->
                if (id != AppConstants.LONG_NO_VALUE) {
                    val currentPlaylist = playlistManager.loadSelectedPlayList()
                    currentPlaylist?.let { currentId ->
                        val indexOfSelected = currentPlaylists.indexOf(currentId)
                        _playlistDataState.update { current ->
                            current.copy(
                                playlistSelectedIndex = indexOfSelected,
                                isDataRequested = false
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
                current.copy(groups = groups, isLoading = false, isDataRequested = true)
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
        val isDataRequested: Boolean = false,
        val playlistSelectedIndex: Int = INT_NO_VALUE,
        val isPlaylistSelectorVisible: Boolean = false
    ) {
        val dataIsEmpty: Boolean
            get() {
                return if (playlists.isEmpty()) true else {
                    isDataRequested && groups.isEmpty()
                }
            }
    }
}