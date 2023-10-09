/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.network.NetworkConnectivityObserver
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.events.PlaybackEvents
import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptv.utils.isMediaPlayable
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase
) : ViewModel() {

    private var _playerUIState = MutableStateFlow(ControlUIState())
    val playerUIState = _playerUIState.asStateFlow()

    private val _playbackEffects: Channel<PlaybackEvents> = Channel()
    val playbackEffects = _playbackEffects.receiveAsFlow()

    init {
        Napier.i("testing VideoViewViewModel init")
        viewModelScope.launch {
            _playerUIState.update {
                it.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()]
                )
            }
        }
        viewModelScope.launch {
            networkConnectivityObserver.observe()
                .collect { status ->
                    val isNetworkOn = status == NetworkConnectivityObserver.Status.Available
                    if (isNetworkOn) {
                        restartPlayingIfNeeded()
                    }
                    _playerUIState.update {
                        it.copy(
                            isOnline = isNetworkOn
                        )
                    }
                }
        }
    }

    private fun restartPlayingIfNeeded() {
        viewModelScope.launch {
            _playbackEffects.send(PlaybackEvents.OnPlaybackRestart)
        }
    }

    fun initPlayBack(channelUrl: String, channelGroup: String) {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(channelGroup)
            Napier.w("testing initPlayBack channelList channelUrl:$channelUrl")
            val currentItemPosition = getCurrentMediaPosition(channelUrl, channelList)
            val programs = getCurrentMediaEpg()
            Napier.w("testing initPlayBack channelList count:${channelList.count()}")
            Napier.w("testing initPlayBack currentItemPosition count:$currentItemPosition")
            Napier.w("testing initPlayBack programs count:${programs.count()}")
            _playerUIState.update { state ->
                state.copy(
                    mediaPosition = currentItemPosition,
                    channels = channelList,
                    epgs = programs
                )
            }
        }
    }

    private fun getCurrentMediaPosition(
        channelUrl: String,
        channels: List<TvPlaylistChannel>
    ): Int {
        val currentPos = playerUIState.value.mediaPosition
        channels.forEach {
            Napier.w("testing getCurrentMediaPosition channel:${it.channelName}")
        }
        val targetPos = channels
            .indexOfFirst { it.channelUrl == channelUrl }

        val mediaPosition = if (currentPos < 0) {
            targetPos
        } else {
            currentPos
        }

        return mediaPosition
    }


    fun processPlaybackActions(action: PlaybackActions) {
        val effect = when (action) {
            PlaybackActions.OnChannelsUiToggle -> PlaybackEvents.OnChannelsUiToggle
            PlaybackActions.OnEpgUiToggle -> PlaybackEvents.OnEpgUiToggle
            PlaybackActions.OnFullScreenToggle -> PlaybackEvents.OnFullScreenToggle
            PlaybackActions.OnNextSelected -> PlaybackEvents.OnNextSelected
            PlaybackActions.OnPlaybackToggle -> PlaybackEvents.OnPlaybackToggle
            PlaybackActions.OnPlayerUiToggle -> PlaybackEvents.OnPlayerUiToggle
            PlaybackActions.OnPreviousSelected -> PlaybackEvents.OnPreviousSelected
            PlaybackActions.OnVideoResizeToggle -> PlaybackEvents.OnVideoResizeToggle
            PlaybackActions.OnVolumeDown -> PlaybackEvents.OnVolumeDown
            PlaybackActions.OnVolumeUp -> PlaybackEvents.OnVolumeUp
            PlaybackActions.OnChannelInfoUiToggle -> PlaybackEvents.OnChannelInfoUiToggle
        }
        viewModelScope.launch {
            _playbackEffects.send(effect)
        }

    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        var isMediaPlayable = playerUIState.value.isMediaPlayable
        var isBuffering = playerUIState.value.isBuffering

        when (action) {
            is PlaybackStateActions.OnMediaItemTransition -> {
                Napier.w("testing index ${action.index}")
                _playerUIState.update { state ->
                    state.copy(
                        currentChannel = action.mediaTitle,
                        epgs = getCurrentMediaEpg(),
                    )
                }

                restartPlayingIfNeeded()
            }

            is PlaybackStateActions.OnPlaybackState -> {
                isBuffering = action.state == VideoPlaybackState.VideoPlaybackBuffering
                Napier.e("testing OnPlaybackState isBuffering:$isBuffering")

                when (action.state) {
                    is VideoPlaybackState.VideoPlaybackIdle -> {
                        Napier.e("testing VideoPlaybackState VideoPlaybackIdle")
                        isMediaPlayable = isMediaPlayable(action.state.errorCode)
                    }

                    VideoPlaybackState.VideoPlaybackReady -> {
                        Napier.e("testing VideoPlaybackState VideoPlaybackReady")
                        isMediaPlayable = true
                    }

                    else -> {

                    }
                }

            }
        }

        _playerUIState.update { state ->
            state.copy(
                isMediaPlayable = isMediaPlayable,
                isBuffering = isBuffering
            )
        }
    }

    private fun getCurrentMediaEpg(): List<EpgProgram> {
        /*        val currentMedia =
                    playerUIState.value.channels.firstOrNull { it.channelName == playerUIState.value.currentChannel }

                return currentMedia?.let { channel ->
                    val id = listOf(channel.epgId).firstNotNullOfOrNull { it }
                    val programs =
                        id?.let { playlistChannelManager.getEpgListForChannel(it) } ?: emptyList()
                    Napier.w("testing getCurrentMediaEpg programs $programs")
                    programs
                } ?: emptyList()*/
        return emptyList()
    }

    fun toggleEpgVisibility() {
        val currentEpgVisibleState = playerUIState.value.isEpgVisible
        if (!currentEpgVisibleState) {
            val programs = getCurrentMediaEpg()
            _playerUIState.update { state ->
                state.copy(epgs = programs)
            }
        }

        _playerUIState.update { state ->
            state.copy(
                isEpgVisible = !currentEpgVisibleState
            )
        }
    }

    data class ControlUIState(
        val currentChannel: String = "",
        val isUseSubtitle: Boolean = false,
        val isTracksAvailable: Boolean = false,
        val isUiVisible: Boolean = false,
        val isEpgVisible: Boolean = false,
        val isFullscreen: Boolean = false,
        val isPlaying: Boolean = false,
        val isBuffering: Boolean = false,
        val isMediaPlayable: Boolean = true,
        val isOnline: Boolean = true,
        val videoSizeRatio: Float = 1.7777778f,
        val videoResizeMode: ResizeMode = ResizeMode.Fit,
        val mediaPosition: Int = -1,
        val channels: List<TvPlaylistChannel> = emptyList(),
        val epgs: List<EpgProgram> = emptyList()
    )
}