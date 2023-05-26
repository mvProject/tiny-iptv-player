/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 22.05.23, 15:54
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.manager.PlaylistChannelManager
import com.mvproject.tinyiptv.data.mappers.EntityMapper.toPlaylistChannelWithEpg
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.tinyiptv.utils.AppConstants.EMPTY_STRING
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistGroupViewModel(
    private val playlistChannelManager: PlaylistChannelManager
) : ViewModel() {
    private val _viewState = MutableStateFlow(GroupChannelsState())
    val viewState = _viewState.asStateFlow()

    val channels = mutableStateListOf<PlaylistChannelWithEpg>()

    private var favoriteIds = emptyList<Long>()

    private val _searchText = MutableStateFlow(EMPTY_STRING)

    init {
        viewModelScope.launch {
            loadFavorites()

            _viewState.update {
                it.copy(viewType = playlistChannelManager.getChannelsViewType())
            }
        }
    }

    fun loadChannelsByGroups(group: String) {
        if (viewState.value.currentGroup != group) {
            Napier.w("testing loadChannelsByGroups group:$group")
            _viewState.update {
                it.copy(currentGroup = group, isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO) {
                channels.addAll(playlistChannelManager.getChannelsByGroup(group)
                    .map {
                        it.toPlaylistChannelWithEpg()
                    }
                )
                _viewState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        _viewState.update {
            it.copy(searchString = text)
        }
    }

    fun onViewTypeChange(type: ChannelsViewType) {
        if (viewState.value.viewType != type) {
            viewModelScope.launch {
                _viewState.update {
                    it.copy(viewType = type)
                }
                playlistChannelManager.setChannelsViewType(type)
            }
        }
    }

    fun onSearchTriggered() {
        val searchState = viewState.value.isSearching
        _viewState.update {
            it.copy(isSearching = !searchState)
        }
    }

    fun toggleFavourites(item: PlaylistChannelWithEpg) {
        val channel = channels.firstOrNull { it.id == item.id }
        channel?.let { chn ->
            val index = channels.indexOf(chn)
            val isFavorite = chn.isInFavorites
            viewModelScope.launch {
                channels[index] = chn.copy(isInFavorites = !isFavorite)

                if (isFavorite) {
                    playlistChannelManager.deleteChannelFromFavorites(chn.id)
                } else {
                    playlistChannelManager.addChannelToFavorites(chn.id, chn.channelName)
                }

                loadFavorites()
            }
        }
    }

    fun updateChannelInfo(item: PlaylistChannelWithEpg) {
        val channel = channels.firstOrNull { it.id == item.id }
        channel?.let { chn ->
            loadFavoritesAndEpgInfo(channel = chn)
        }
    }

    private fun loadFavoritesAndEpgInfo(channel: PlaylistChannelWithEpg) {
        val index = channels.indexOf(channel)
        Napier.w("testing loadFavoritesInfo channel:${channel.channelName}, index:$index, from ${channels.count()}")
        val isInFavorites = channel.id in favoriteIds
        val programs =
            channel.epgId?.let { playlistChannelManager.getEpgForChannel(it) } ?: emptyList()
        val isShouldUpdate = channel.isInFavorites != isInFavorites || programs.isNotEmpty()
        Napier.w("testing loadFavoritesInfo isShouldUpdate:$isShouldUpdate")
        if (isShouldUpdate) {
            channels[index] = channel.copy(
                isInFavorites = isInFavorites,
                channelEpg = programs
            )
        }
    }

    private suspend fun loadFavorites() {
        favoriteIds = playlistChannelManager.getFavoriteIds()
    }

    data class GroupChannelsState(
        val currentGroup: String = EMPTY_STRING,
        val isLoading: Boolean = false,
        val isSearching: Boolean = false,
        val searchString: String = EMPTY_STRING,
        val viewType: ChannelsViewType = ChannelsViewType.LIST
    )

    override fun onCleared() {
        super.onCleared()
        Napier.e("testing onCleared")
    }
}