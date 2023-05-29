/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 12:54
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.data.manager.PlaylistManager
import com.mvproject.tinyiptv.data.models.playlist.Playlist
import com.mvproject.tinyiptv.ui.screens.playlist.actions.PlaylistDetailAction
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_LOCAL_TYPE
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_REMOTE_SEC_TYPE
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_REMOTE_TYPE
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddPlayListViewModel(
    private val playlistManager: PlaylistManager
) : ViewModel() {

    private val _state = MutableStateFlow(PlayListState())
    val state = _state.asStateFlow()

    fun setPlaylistMode(id: String) {
        id.toLongOrNull()?.let { selectedId ->
            viewModelScope.launch(Dispatchers.IO) {
                val currentPlaylist = playlistManager.loadPlaylistById(playlistId = selectedId)
                currentPlaylist?.let { playlist ->

                    _state.update { current ->
                        current.copy(
                            selectedId = selectedId,
                            isEdit = true,
                            listName = playlist.listName,
                            isLocal = playlist.listUrl.isLocalPlaylist(),
                            isRemote = playlist.listUrl.isRemotePlaylist(),
                            listUri = playlist.listUrl,
                            lastUpdateDate = playlist.lastUpdateDate,
                            updatePeriod = playlist.updatePeriod.toInt(),
                            isUsingAlterEpg = playlist.isAlterInfoUse,
                            isUsingMainEpg = playlist.isMainInfoUse
                        )
                    }
                }
            }
        }
    }

    fun processAction(action: PlaylistDetailAction) {
        when (action) {
            PlaylistDetailAction.SavePlaylist -> {
                savePlayList()
            }

            is PlaylistDetailAction.ChangeName -> {
                _state.update { current ->
                    current.copy(isComplete = false, listName = action.newName)
                }
            }

            is PlaylistDetailAction.ChangeUpdatePeriod -> {
                _state.update { current ->
                    current.copy(isComplete = false, updatePeriod = action.period)
                }
            }

            is PlaylistDetailAction.ChangeMainEpgUsingState -> {
                _state.update { current ->
                    current.copy(isComplete = false, isUsingMainEpg = action.state)
                }
            }

            is PlaylistDetailAction.ChangeAlterEpgUsingState -> {
                _state.update { current ->
                    current.copy(isComplete = false, isUsingAlterEpg = action.state)
                }
            }

            is PlaylistDetailAction.ChangeUrl -> {
                _state.update { current ->
                    current.copy(
                        isComplete = false,
                        listUri = action.newUrl,
                        isLocal = action.newUrl.isLocalPlaylist(),
                        isRemote = action.newUrl.isRemotePlaylist()
                    )
                }
            }

            else -> {}
        }
    }

    private fun String.isLocalPlaylist() = this.contains(PLAYLIST_LOCAL_TYPE)

    private fun String.isRemotePlaylist() =
        this.contains(PLAYLIST_REMOTE_TYPE) || this.contains(
            PLAYLIST_REMOTE_SEC_TYPE
        )

    private fun savePlayList() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(isSaving = true)
            }
            runCatching {
                playlistManager.savePlaylist(
                    playlist = state.value.toPlaylist()
                )
            }.onSuccess {
                _state.update { current ->
                    current.copy(isComplete = true, isSaving = false)
                }
            }.onFailure {
                Napier.e("testing savePlaylist error ${it.localizedMessage}")
                _state.update { current ->
                    current.copy(isComplete = false, isSaving = false)
                }
            }
        }
    }
}


data class PlayListState(
    val listName: String = EMPTY_STRING,
    val listUri: String = EMPTY_STRING,
    val isLocal: Boolean = false,
    val isRemote: Boolean = true,
    val updatePeriod: Int = UpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val selectedId: Long = Random.nextLong(),
    val isUsingMainEpg: Boolean = true,
    val isUsingAlterEpg: Boolean = false,
    val isComplete: Boolean = false,
) {

    val isReadyToSave
        get() = listUri.isNotEmpty() && listName.isNotEmpty()

    fun toPlaylist() = with(this) {
        Playlist(
            id = selectedId,
            listName = listName,
            listUrl = listUri,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod.toLong(),
            isRemoteSource = isRemote,
            isMainInfoUse = isUsingMainEpg,
            isAlterInfoUse = isUsingAlterEpg
        )
    }
}