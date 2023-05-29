/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:32
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.manager.PlaylistManager
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.utils.AppConstants.LONG_NO_VALUE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPlaylistViewModel(
    private val playlistManager: PlaylistManager,
) : ViewModel() {

    private val _playlistDataState = MutableStateFlow(PlaylistSettingsState())
    val playlistDataState = _playlistDataState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistManager.allPlaylistsFlow.collect {
                _playlistDataState.update { state ->
                    state.copy(playlists = playlistManager.loadPlaylists(), isLoading = false)
                }
            }
        }
    }

    fun deletePlaylist(playlistId: String) {
        playlistId.toLongOrNull()?.let { id ->
            viewModelScope.launch {
                val ids = playlistDataState.value.playlists.map { it.id }
                val currentId = playlistManager.currentPlaylistId.first()

                if (currentId == id) {
                    playlistManager.setCurrentPlaylist(ids.firstOrNull { it != currentId }
                        ?: LONG_NO_VALUE)
                }

                playlistManager.deletePlaylist(id)
            }
        }
    }

    data class PlaylistSettingsState(
        val playlists: List<Playlist> = emptyList(),
        val isLoading: Boolean = true,
    ) {
        val dataIsEmpty
            get() = !isLoading && playlists.isEmpty()
    }
}