/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
import androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
import androidx.media3.common.PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.data.enums.player.PlayerCommands
import com.mvproject.tinyiptv.data.enums.player.PlayerUICommands
import com.mvproject.tinyiptv.data.enums.player.ViewSettingsRequest
import com.mvproject.tinyiptv.data.helpers.ViewSettingsHelper
import com.mvproject.tinyiptv.data.manager.PlaylistChannelManager
import com.mvproject.tinyiptv.data.mappers.ListMappers.createMediaItems
import com.mvproject.tinyiptv.data.models.channels.PlaylistChannel
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.data.network.NetworkConnectivityObserver
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.utils.aspectRatio
import com.mvproject.tinyiptv.utils.calculateProperBrightnessRange
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoViewViewModel(
    val player: Player,
    private val viewSettingsHelper: ViewSettingsHelper,
    private val preferenceRepository: PreferenceRepository,
    private val playlistChannelManager: PlaylistChannelManager,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private var _playerUIState = MutableStateFlow(
        ControlUIState(
            brightnessValue = viewSettingsHelper.currentScreenBrightness,
            volumeValue = viewSettingsHelper.volume
        )
    )
    val playerUIState = _playerUIState.asStateFlow()

    private var _currentEpg = MutableStateFlow<List<EpgProgram>>(emptyList())
    val currentEpg = _currentEpg.asStateFlow()

    private var isNetworkOn = true
    private var channelList = emptyList<PlaylistChannel>()

    var channelsEpgs = emptyList<EpgProgram>()
        private set

    init {
        Napier.i("testing VideoViewViewModel init")
        viewModelScope.launch {
            _playerUIState.update {
                it.copy(
                    isFullscreen = preferenceRepository.getDefaultFullscreenMode(),
                    videoResizeMode = ResizeMode.values()[preferenceRepository.getDefaultResizeMode()]
                )
            }
        }
        viewModelScope.launch {
            networkConnectivityObserver.observe()
                .collect { status ->
                    isNetworkOn = status == NetworkConnectivityObserver.Status.Available
                    if (isNetworkOn) {
                        restartPlayingIfNeeded()
                    }
                }
        }
    }

    private fun restartPlayingIfNeeded() {
        if (player.playbackState == Player.STATE_IDLE) {
            Napier.w("testing restartPlayingIfNeeded")
            player.playWhenReady = true
            player.prepare()
        }
    }

    private fun setListener() {
        player.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val data = mediaItem?.mediaMetadata?.displayTitle.toString()
                    Napier.w("testing onMediaItemTransition data $data")
                    _playerUIState.update { state ->
                        state.copy(
                            isUiVisible = true,
                            currentChannel = data
                        )
                    }

                    restartPlayingIfNeeded()
                    getCurrentMediaEpg()
                }

                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    _playerUIState.update { state ->
                        state.copy(videoSizeRatio = videoSize.aspectRatio())
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Napier.e("testing onPlayerError code:${error.errorCode}, codeName:${error.errorCodeName}")
                }

                override fun onPlaybackStateChanged(playbackState: Int) {

                    var isOnline = playerUIState.value.isOnline
                    var isMediaPlayable = playerUIState.value.isMediaPlayable

                    if (playbackState == Player.STATE_READY) {
                        Napier.i("testing onPlaybackStateChanged playbackState STATE_READY")
                        isMediaPlayable = true
                    }

                    if (playbackState == Player.STATE_BUFFERING) {
                        Napier.w("testing onPlaybackStateChanged playbackState STATE_BUFFERING")
                        isOnline = true
                    }

                    if (playbackState == Player.STATE_IDLE) {
                        Napier.e("testing onPlaybackStateChanged playbackState STATE_IDLE")

                        val errorCode = player.playerError?.errorCode ?: -1

                        if (errorCode == ERROR_CODE_IO_NETWORK_CONNECTION_FAILED) {
                            if (isNetworkOn) {
                                isMediaPlayable = false
                            } else {
                                isOnline = false
                            }
                        }

                        if (errorCode == ERROR_CODE_IO_BAD_HTTP_STATUS ||
                            errorCode == ERROR_CODE_PARSING_MANIFEST_MALFORMED
                        ) {
                            isMediaPlayable = false
                        }

                    }
                    if (playbackState == Player.STATE_ENDED) {
                        Napier.e("testing onPlaybackStateChanged playbackState STATE_ENDED")
                    }

                    _playerUIState.update { state ->
                        state.copy(
                            isBuffering = playbackState == Player.STATE_BUFFERING,
                            isOnline = isOnline,
                            isMediaPlayable = isMediaPlayable,
                            mediaPosition = player.currentMediaItemIndex
                        )
                    }
                }

                override fun onRenderedFirstFrame() {
                    super.onRenderedFirstFrame()
                    Napier.e("testing onRenderedFirstFrame")
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playerUIState.update { state ->
                        state.copy(isPlaying = isPlaying)
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        delay(3000L)
                        _playerUIState.update { state ->
                            state.copy(isUiVisible = false)
                        }
                    }
                }

                override fun onTracksChanged(tracks: Tracks) {
                    tracks.groups.forEachIndexed { _, group ->
                        Napier.i("testing playerInfo group ${group.type}")
                        if (group.type == C.TRACK_TYPE_AUDIO) {
                            for (i in 0..group.length - 1) {
                                val trackFormat = group.getTrackFormat(i)
                                val audioTrack = trackFormat
                                val audioTrackLanguage = trackFormat.language.toString()
                                val audioTrackLabel = trackFormat.label.toString()
                                Napier.i("testing group audioTrack $audioTrack")
                                Napier.i("testing group audioTrackLanguage $audioTrackLanguage")
                                Napier.i("testing group audioTrackLabel $audioTrackLabel")
                            }
                        }
                    }
                }
            }
        )
    }

    fun initPlayBack(channelId: String, channelGroup: String) {
        Napier.w("testing initPlayBack channelId $channelId, channelGroup:$channelGroup")

        viewModelScope.launch {
            channelList = playlistChannelManager.getChannelsByGroup(channelGroup)

            Napier.w("testing initPlayBack channelList ${channelList.count()}")

            setListener()

            player.setMediaItems(
                channelList.createMediaItems(),
                getCurrentMediaPosition(channelId),
                0L
            )

            player.repeatMode = Player.REPEAT_MODE_ALL

            player.playWhenReady = true

            player.prepare()
        }
    }

    private fun getCurrentMediaPosition(channelId: String): Int {
        val currentPos = playerUIState.value.mediaPosition
        val targetPos = channelList.indexOfFirst { it.id == channelId.toLong() }
        val mediaPosition = if (currentPos < 0) {
            targetPos
        } else {
            currentPos
        }
        return mediaPosition
    }


    fun processPlayerUICommand(command: PlayerUICommands) {
        Napier.i("processPlayerUICommand received $command")
        when (command) {
            PlayerUICommands.CONTROL_UI_ON -> showControlUi()
            PlayerUICommands.CONTROL_UI_OFF -> hideControlUi()
            PlayerUICommands.TOGGLE_UI_VISIBILITY -> toggleControlUi()
            PlayerUICommands.TOGGLE_EPG_VISIBILITY -> toggleEpgVisibility()
            PlayerUICommands.TOGGLE_FULL_SCREEN -> toggleFullScreenMode()
            PlayerUICommands.TOGGLE_RESIZE_MODE -> toggleVideoResizeMode()
        }
    }

    fun processPlayerCommand(command: PlayerCommands) {
        Napier.i("processPlayerCommand received $command")
        when (command) {
            PlayerCommands.START_PLAY -> player.play()
            PlayerCommands.STOP_PLAY -> player.pause()
            PlayerCommands.NEXT_VIDEO -> player.seekToNextMediaItem()
            PlayerCommands.PREVIOUS_VIDEO -> player.seekToPreviousMediaItem()
            PlayerCommands.PLAYBACK_TOGGLE -> {
                if (playerUIState.value.isPlaying)
                    player.pause()
                else player.play()
            }

        }
    }

    fun processViewSettingsChange(command: ViewSettingsRequest) {
        Napier.i("processViewSettingsChange received $command")
        when (command) {
            ViewSettingsRequest.VOLUME_UP -> {
                updateVolume() {
                    viewSettingsHelper.volumeUp()
                }
            }

            ViewSettingsRequest.VOLUME_DOWN -> {
                updateVolume() {
                    viewSettingsHelper.volumeDown()
                }
            }

            ViewSettingsRequest.BRIGHTNESS_UP -> {
                val updatedValue = playerUIState.value.brightnessValue + 1
                updateBrightness(value = updatedValue)
            }

            ViewSettingsRequest.BRIGHTNESS_DOWN -> {
                val updatedValue = playerUIState.value.brightnessValue - 1
                updateBrightness(value = updatedValue)
            }

            ViewSettingsRequest.NONE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1500)
                    _playerUIState.update { state ->
                        state.copy(
                            isBrightnessChanging = false,
                            isVolumeChanging = false,
                        )
                    }
                }
            }
        }
    }

    private fun toggleVideoResizeMode() {
        val currentMode = playerUIState.value.videoResizeMode
        val targetMode = ResizeMode.toggleResizeMode(current = currentMode)
        _playerUIState.update { state ->
            state.copy(videoResizeMode = targetMode)
        }
    }


    private fun updateVolume(action: () -> Unit) {
        _playerUIState.update { state ->
            state.copy(
                isVolumeChanging = true,
                volumeValue = viewSettingsHelper.volume
            )
        }

        action()
    }

    private fun updateBrightness(value: Int) {
        val actualValue = calculateProperBrightnessRange(value)

        _playerUIState.update { state ->
            state.copy(
                isBrightnessChanging = true,
                brightnessValue = actualValue
            )
        }
    }

    private fun showControlUi() {
        _playerUIState.update { state ->
            state.copy(isUiVisible = true)
        }
    }

    private fun toggleControlUi() {
        val current = playerUIState.value.isUiVisible
        _playerUIState.update { state ->
            state.copy(isUiVisible = !current)
        }
    }

    private fun getCurrentMediaEpg() {
        val currentMedia =
            channelList.firstOrNull { it.channelName == playerUIState.value.currentChannel }

        currentMedia?.let { channel ->
            val id = listOf(channel.epgId, channel.epgAlterId).firstNotNullOfOrNull { it }
            val programs =
                id?.let { playlistChannelManager.getEpgListForChannel(it) } ?: emptyList()
            _currentEpg.value = programs
            Napier.w("testing getCurrentMediaEpg programs $programs")
        }
    }

    fun toggleEpgVisibility() {
        val currentMedia =
            channelList.firstOrNull { it.channelName == playerUIState.value.currentChannel }
        currentMedia?.let { channel ->
            val id = listOf(channel.epgId, channel.epgAlterId).firstNotNullOfOrNull { it }
            val programs =
                id?.let { playlistChannelManager.getEpgListForChannel(it) } ?: emptyList()
            channelsEpgs = programs
            Napier.w("testing toggleEpgVisibility programs $programs")
        }

        val currentEpgVisibleState = playerUIState.value.isEpgVisible
        _playerUIState.update { state ->
            state.copy(isEpgVisible = !currentEpgVisibleState)
        }
    }

    /*    private fun refreshEpg() {
            val currentMedia =
                channelList.firstOrNull { it.channelName == playerUIState.value.currentChannel }

            currentMedia?.let { channel ->
                val id = listOf(channel.epgId, channel.epgAlterId).firstNotNullOfOrNull { it }
                Napier.e("testing getCurrentMediaEpg channel ${channel.channelName}, ids:$id")
                // val programs = id?.let { playlistChannelManager.getEpgForChannel(it) } ?: emptyList()
                val programs =
                    id?.let { playlistChannelManager.getEpgListForChannel(it) } ?: emptyList()
                _currentEpg.value = programs
                Napier.w("testing getCurrentMediaEpg programs $programs")
            }
        }*/


    private fun toggleFullScreenMode() {
        val current = playerUIState.value.isFullscreen
        _playerUIState.update { state ->
            state.copy(isFullscreen = !current)
        }
    }

    private fun hideControlUi() {
        _playerUIState.update { state ->
            state.copy(isUiVisible = false)
        }
    }


    fun cleanPlayback() {
        Napier.i("testing1 VideoViewViewModel cleanPlayback")
        player.stop()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        Napier.i("testing1 VideoViewViewModel onCleared")
    }

    data class ControlUIState(
        val brightnessValue: Int,
        val volumeValue: Int,
        val currentChannel: String = "",
        val isBrightnessChanging: Boolean = false,
        val isVolumeChanging: Boolean = false,
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
        val mediaPosition: Int = -1
    )
}