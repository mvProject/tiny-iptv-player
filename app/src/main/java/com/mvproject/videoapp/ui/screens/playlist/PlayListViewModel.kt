/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.ui.screens.playlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.videoapp.data.enums.playlist.PlaylistUpdatePeriod
import com.mvproject.videoapp.data.manager.PlaylistManager
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING
import com.mvproject.videoapp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_LOCAL_TYPE
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_REMOTE_SEC_TYPE
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_REMOTE_TYPE
import com.mvproject.videoapp.utils.ContextUtil
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddPlayListViewModel(
    private val contextUtil: ContextUtil,
    private val playlistManager: PlaylistManager
) : ViewModel() {

    private val _state = MutableStateFlow(PlayListState())
    val state = _state.asStateFlow()

    fun setPlaylistMode(id: String) {
        id.toLongOrNull()?.let { selectedId ->
            viewModelScope.launch(Dispatchers.IO) {
                val currentPlaylist = playlistManager.loadPlaylistById(playlistId = selectedId)
                currentPlaylist?.let { playlist ->
                    val isLocal = playlist.listUrl.contains(PLAYLIST_LOCAL_TYPE)

                    _state.update { current ->
                        current.copy(
                            selectedId = selectedId,
                            isEdit = true,
                            listName = playlist.listName,
                            listDisplayUri = playlist.listUrl,
                            isLocal = isLocal,
                            listUri = playlist.listUrl,
                            lastUpdateDate = playlist.lastUpdateDate,
                            updatePeriod = playlist.updatePeriod.toInt()
                        )
                    }
                }
            }
        }
    }

    fun processAction(action: PlayListAction) {
        when (action) {
            PlayListAction.SavePlaylist -> {
                savePlayList()
            }

            is PlayListAction.ChangeName -> {
                _state.update { current ->
                    current.copy(listName = action.newName)
                }
            }

            is PlayListAction.ChangeUpdatePeriod -> {
                _state.update { current ->
                    current.copy(updatePeriod = action.period)
                }
            }

            is PlayListAction.ChangeUrl -> {
                val isLocal = action.newUrl.contains(PLAYLIST_LOCAL_TYPE)
                val isRemote =
                    action.newUrl.contains(PLAYLIST_REMOTE_TYPE) || action.newUrl.contains(
                        PLAYLIST_REMOTE_SEC_TYPE
                    )
                Napier.w("testing PlayListAction.ChangeUrl isLocal: $isLocal, isRemote: $isRemote")
                val displayUri = properLocalDisplayUri(isLocal, action.newUrl)
                val fileName = properLocalDisplayName(isLocal, displayUri)
                _state.update { current ->
                    current.copy(
                        listName = fileName,
                        listDisplayUri = displayUri,
                        listUri = action.newUrl,
                        isLocal = isLocal
                    )
                }
            }
        }
    }

    private fun properLocalDisplayName(isLocal: Boolean, displayUri: String): String {
        return if (isLocal && displayUri.isNotEmpty()) {
            displayUri.split(".").dropLast(1).joinToString("_")
        } else EMPTY_STRING
    }

    private fun properLocalDisplayUri(isLocal: Boolean, stringUri: String): String {
        return if (isLocal) {
            contextUtil.getFileNameFromUri(Uri.parse(stringUri)) ?: EMPTY_STRING
        } else stringUri
    }

    private fun savePlayList() {
        viewModelScope.launch {
            _state.update { current ->
                current.copy(isSaving = true)
            }
            runCatching {
                playlistManager.savePlaylist(state.value.toPlaylist())
            }.onSuccess {
                _state.update { current ->
                    current.copy(isComplete = true, isSaving = false)
                }
            }.onFailure {
                Napier.e("testing savePlaylist error")
                _state.update { current ->
                    current.copy(isComplete = false, isSaving = false)
                }
            }
        }
    }
}


data class PlayListState(
    val listName: String = EMPTY_STRING,
    val listDisplayUri: String = EMPTY_STRING,
    val listUri: String = EMPTY_STRING,
    val isLocal: Boolean = false,
    val updatePeriod: Int = PlaylistUpdatePeriod.NO_UPDATE.value,
    val lastUpdateDate: Long = LONG_VALUE_ZERO,
    val isSaving: Boolean = false,
    val isEdit: Boolean = false,
    val selectedId: Long? = null,
    val isComplete: Boolean = false,
) {

    val isReadyToSave
        get() = listUri.isNotEmpty() && listName.isNotEmpty()

    fun toPlaylist() = with(this) {
        Playlist(
            id = Random.nextLong(),
            listName = listName,
            listUrl = listUri,
            lastUpdateDate = lastUpdateDate,
            updatePeriod = updatePeriod.toLong(),
            isRemoteSource = !isLocal
        )
    }
}

sealed class PlayListAction {
    data class ChangeName(val newName: String) : PlayListAction()
    data class ChangeUrl(val newUrl: String) : PlayListAction()
    data class ChangeUpdatePeriod(val period: Int) : PlayListAction()
    object SavePlaylist : PlayListAction()
}