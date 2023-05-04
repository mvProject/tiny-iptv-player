/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.manager.PlaylistManager
import com.mvproject.videoapp.data.models.playlist.Playlist
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
            playlistManager.allPlaylistsFlow.collect { playlists ->
                _playlistDataState.update { state ->
                    state.copy(playlists = playlistManager.loadPlaylists())
                }
            }
        }
    }

    fun deletePlaylist(playlistId: String?) {
        playlistId?.toLongOrNull()?.let { id ->
            viewModelScope.launch {
                val ids = playlistDataState.value.playlists.map { it.id }
                val currentId = playlistManager.currentPlaylistId.first()

                if (currentId == id) {
                    playlistManager.setCurrentPlaylist(ids.first { it != currentId })
                }

                playlistManager.deletePlaylist(id)
            }
        }
    }

    data class PlaylistSettingsState(
        val playlists: List<Playlist> = emptyList(),
        val isLoading: Boolean = true,
    )
}