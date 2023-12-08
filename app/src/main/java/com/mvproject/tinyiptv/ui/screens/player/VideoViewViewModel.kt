/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 13:05
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvproject.tinyiptv.data.enums.RatioMode
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.mappers.ListMappers.withRefreshedEpg
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.data.network.NetworkConnectivityObserver
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.usecases.GetGroupChannelsUseCase
import com.mvproject.tinyiptv.data.usecases.ToggleFavoriteChannelUseCase
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackActions
import com.mvproject.tinyiptv.ui.screens.player.action.PlaybackStateActions
import com.mvproject.tinyiptv.ui.screens.player.events.PlaybackEvents
import com.mvproject.tinyiptv.ui.screens.player.state.VideoPlaybackState
import com.mvproject.tinyiptv.ui.screens.player.state.VideoViewState
import com.mvproject.tinyiptv.utils.AppConstants.DELAY_50
import com.mvproject.tinyiptv.utils.AppConstants.FLOAT_STEP_VOLUME
import com.mvproject.tinyiptv.utils.AppConstants.FLOAT_VALUE_1
import com.mvproject.tinyiptv.utils.AppConstants.FLOAT_VALUE_ZERO
import com.mvproject.tinyiptv.utils.AppConstants.INT_NO_VALUE
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.tinyiptv.utils.AppConstants.UI_SHOW_DELAY
import com.mvproject.tinyiptv.utils.AppConstants.VOLUME_SHOW_DELAY
import com.mvproject.tinyiptv.utils.isMediaPlayable
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val getGroupChannelsUseCase: GetGroupChannelsUseCase,
    private val toggleFavoriteChannelUseCase: ToggleFavoriteChannelUseCase,
) : ViewModel() {

    private var pollVideoPositionJob: Job? = null
    private var pollVolumeJob: Job? = null

    //time after which [VideoViewState.isControlUiVisible] will be set to false
    private var hideControllerAfterMs: Long = UI_SHOW_DELAY

    //time after which [VideoViewState.isVolumeUiVisible] will be set to false
    private var hideVolumeAfterMs: Long = VOLUME_SHOW_DELAY

    private var _videoRatio = FLOAT_VALUE_1

    private var _videoViewState = MutableStateFlow(VideoViewState())
    val videoViewState = _videoViewState.asStateFlow()

    private val _playbackEffects: Channel<PlaybackEvents> = Channel()
    val playbackEffects = _playbackEffects.receiveAsFlow()

    init {
        Napier.i("testing VideoViewViewModel init")
        viewModelScope.launch {
            _videoViewState.update { current ->
                val ratioMode = RatioMode.entries[preferenceRepository.getDefaultRatioMode()]

                current.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.entries[preferenceRepository.getDefaultResizeMode()],
                    videoRatioMode = ratioMode,
                    videoRatio = ratioMode.ratio
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
                    _videoViewState.update {
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

    fun initPlayBack(channelName: String, channelGroup: String) {
        viewModelScope.launch {
            val channelList = getGroupChannelsUseCase(channelGroup)
            val currentItemPosition = getCurrentMediaPosition(
                channelName = channelName,
                channels = channelList
            )
            val currentChannel = channelList[currentItemPosition]

            _videoViewState.update { current ->
                current.copy(
                    channelGroup = channelGroup,
                    mediaPosition = currentItemPosition,
                    channels = channelList,
                    currentChannel = currentChannel
                )
            }
        }
    }

    fun switchToChannel(channel: TvPlaylistChannel) {
        viewModelScope.launch {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()

            val newMediaPosition = getCurrentMediaPosition(
                channelName = channel.channelName,
                channels = channelsRefreshed
            )

            setCurrentChannel(currentMediaPosition = newMediaPosition)
        }
    }

    fun processPlaybackActions(action: PlaybackActions) {
        when (action) {
            PlaybackActions.OnNextSelected -> switchToNextChannel()
            PlaybackActions.OnPreviousSelected -> switchToPreviousChannel()
            PlaybackActions.OnChannelsUiToggle -> toggleChannelsVisibility()
            PlaybackActions.OnEpgUiToggle -> toggleEpgVisibility()
            PlaybackActions.OnFullScreenToggle -> toggleFullScreen()
            PlaybackActions.OnVideoResizeToggle -> toggleVideoResizeMode()
            PlaybackActions.OnVideoRatioToggle -> toggleVideoRatioMode()
            PlaybackActions.OnChannelInfoUiToggle -> toggleChannelInfoVisibility()
            PlaybackActions.OnFavoriteToggle -> toggleChannelFavorite()
            PlaybackActions.OnPlaybackToggle -> togglePlayingState()
            PlaybackActions.OnPlayerUiToggle -> toggleControlUiState()
            PlaybackActions.OnVolumeDown -> decreaseVolume()
            PlaybackActions.OnVolumeUp -> increaseVolume()
            PlaybackActions.OnRestarted -> consumeRestart()
        }
    }

    fun processPlaybackStateActions(action: PlaybackStateActions) {
        when (action) {
            is PlaybackStateActions.OnVideoSizeChanged -> {
                _videoRatio = action.videoRatio

                val ratio = if (videoViewState.value.videoRatioMode == RatioMode.Original) {
                    _videoRatio
                } else
                    videoViewState.value.videoRatioMode.ratio

                _videoViewState.update { current ->
                    current.copy(videoRatio = ratio)
                }
            }

            is PlaybackStateActions.OnIsPlayingChanged -> {
                _videoViewState.update { current ->
                    current.copy(isPlaying = action.state)
                }
            }

            is PlaybackStateActions.OnMediaItemTransition -> {
                triggerRestart()
            }

            is PlaybackStateActions.OnPlaybackStateChanged -> {
                var isMediaPlayable = videoViewState.value.isMediaPlayable
                val isBuffering = action.state == VideoPlaybackState.VideoPlaybackBuffering

                when (action.state) {
                    is VideoPlaybackState.VideoPlaybackIdle -> {
                        isMediaPlayable = isMediaPlayable(action.state.errorCode)
                    }

                    VideoPlaybackState.VideoPlaybackReady -> {
                        isMediaPlayable = true
                    }

                    else -> {

                    }
                }

                _videoViewState.update { current ->
                    current.copy(
                        isMediaPlayable = isMediaPlayable,
                        isBuffering = isBuffering
                    )
                }
            }
        }
    }

    fun toggleEpgVisibility() {
        val currentEpgVisibleState = videoViewState.value.isEpgVisible

        if (!currentEpgVisibleState) {
            val channelsRefreshed = videoViewState.value.channels.withRefreshedEpg()
            _videoViewState.update { current ->
                current.copy(channels = channelsRefreshed)
            }
        }

        _videoViewState.update { current ->
            current.copy(isEpgVisible = !currentEpgVisibleState)
        }
    }

    fun toggleChannelsVisibility() {
        val currentChannelsVisibleState = videoViewState.value.isChannelsVisible
        _videoViewState.update { current ->
            current.copy(isChannelsVisible = !currentChannelsVisibleState)
        }
    }

    fun toggleChannelInfoVisibility() {
        val currentChannelInfoVisibleState = videoViewState.value.isChannelInfoVisible
        _videoViewState.update { current ->
            current.copy(isChannelInfoVisible = !currentChannelInfoVisibleState)
        }
    }

    private fun getCurrentMediaPosition(
        channelName: String,
        channels: List<TvPlaylistChannel>
    ): Int {
        val currentPos = videoViewState.value.mediaPosition

        val targetPos = channels
            .indexOfFirst { it.channelName == channelName }

        val mediaPosition = if (targetPos > INT_NO_VALUE) {
            targetPos
        } else {
            currentPos
        }

        return mediaPosition
    }

    private fun toggleFullScreen() {
        val currentFullscreenState = videoViewState.value.isFullscreen
        _videoViewState.update { current ->
            current.copy(isFullscreen = !currentFullscreenState)
        }
    }

    private fun toggleChannelFavorite() {
        val currentChannel = videoViewState.value.currentChannel
        val currentChannels = videoViewState.value.channels
        val currentIndex = videoViewState.value.mediaPosition

        val currentChannelFavoriteChanged = currentChannel.copy(
            isInFavorites = !currentChannel.isInFavorites
        )

        val updatedFavoriteChangedChannels = currentChannels.mapIndexed { index, channel ->
            if (index == currentIndex) {
                currentChannelFavoriteChanged
            } else
                channel
        }

        viewModelScope.launch {
            _videoViewState.update { current ->
                current.copy(
                    currentChannel = currentChannelFavoriteChanged,
                    channels = updatedFavoriteChangedChannels
                )
            }

            toggleFavoriteChannelUseCase(channel = currentChannel)
        }

    }

    private fun triggerRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = true)
        }
        showControlUi()
    }

    private fun consumeRestart() {
        _videoViewState.update { current ->
            current.copy(isRestartRequired = false)
        }
    }

    private fun switchToNextChannel() {
        val currentChannelsCount = videoViewState.value.channels.count()
        val nextIndex = videoViewState.value.mediaPosition + INT_VALUE_1
        val newMediaPosition =
            if (nextIndex > currentChannelsCount - INT_VALUE_1) INT_VALUE_ZERO else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
    }

    private fun switchToPreviousChannel() {
        val currentChannelsCount = videoViewState.value.channels.count()
        val nextIndex = videoViewState.value.mediaPosition - INT_VALUE_1
        val newMediaPosition =
            if (nextIndex < INT_VALUE_ZERO) currentChannelsCount - INT_VALUE_1 else nextIndex

        setCurrentChannel(currentMediaPosition = newMediaPosition)
    }

    private fun increaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = videoViewState.value.currentVolume + FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtMost(FLOAT_VALUE_1)
            _videoViewState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun decreaseVolume() {
        showVolumeUi()
        viewModelScope.launch {
            val targetVolume = videoViewState.value.currentVolume - FLOAT_STEP_VOLUME
            val nextVolume = targetVolume.coerceAtLeast(FLOAT_VALUE_ZERO)
            _videoViewState.update { current ->
                current.copy(currentVolume = nextVolume)
            }
            delay(DELAY_50)
        }
    }

    private fun setCurrentChannel(
        currentMediaPosition: Int
    ) {
        val currentChannels = videoViewState.value.channels.withRefreshedEpg()
        val currentChannel = currentChannels[currentMediaPosition]

        _videoViewState.update { current ->
            current.copy(
                mediaPosition = currentMediaPosition,
                currentChannel = currentChannel,
                channels = currentChannels,
                isChannelsVisible = false
            )
        }
    }

    private fun toggleVideoResizeMode() {
        val currentMode = videoViewState.value.videoResizeMode
        val nextMode = ResizeMode.toggleResizeMode(current = currentMode)
        _videoViewState.update { current ->
            current.copy(videoResizeMode = nextMode)
        }
    }

    private fun toggleVideoRatioMode() {
        val currentMode = videoViewState.value.videoRatioMode
        val nextMode = RatioMode.toggleRatioMode(current = currentMode)

        val nextRatio = if (nextMode == RatioMode.Original)
            _videoRatio
        else nextMode.ratio

        _videoViewState.update { current ->
            current.copy(
                videoRatioMode = nextMode,
                videoRatio = nextRatio
            )
        }
    }

    private fun toggleControlUiState() {
        val currentUiVisibleState = videoViewState.value.isControlUiVisible
        _videoViewState.update { current ->
            current.copy(isControlUiVisible = !currentUiVisibleState)
        }
    }

    private fun togglePlayingState() {
        val currentPlayingState = videoViewState.value.isPlaying
        _videoViewState.update { current ->
            current.copy(isPlaying = !currentPlayingState)
        }
    }

    private fun showControlUi() {
        _videoViewState.update { current ->
            current.copy(isControlUiVisible = true)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = viewModelScope.launch {
            delay(hideControllerAfterMs)
            hideControlUi()
        }
    }

    private fun hideControlUi() {
        _videoViewState.update { current ->
            current.copy(isControlUiVisible = false)
        }
        pollVideoPositionJob?.cancel()
        pollVideoPositionJob = null
    }

    private fun showVolumeUi() {
        _videoViewState.update { current ->
            current.copy(isVolumeUiVisible = true)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = viewModelScope.launch {
            delay(hideVolumeAfterMs)
            hideVolumeUi()
        }
    }

    private fun hideVolumeUi() {
        _videoViewState.update { current ->
            current.copy(isVolumeUiVisible = false)
        }
        pollVolumeJob?.cancel()
        pollVolumeJob = null
    }
}