/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:26
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.mappers.ListMappers.createMediaItems
import com.mvproject.tinyiptv.ui.components.epg.PlayerOverlayEpg
import com.mvproject.tinyiptv.ui.components.epg.PlayerOverlayEpgView
import com.mvproject.tinyiptv.ui.components.player.PlayerView
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptv.ui.components.views.VolumeProgressView
import com.mvproject.tinyiptv.ui.screens.player.events.PlaybackEvents
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.PLAYBACK_START_POSITION
import com.mvproject.tinyiptv.utils.findActivity
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun VideoView(
    viewModel: VideoViewViewModel,
    channelUrl: String,
    channelGroup: String
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val windowSizeClass = calculateWindowSizeClass(activity)
    val displayFeatures = calculateDisplayFeatures(activity)

    val playbackState by viewModel.playerUIState.collectAsState()

    val playerState = rememberVideoPlayerState(
        isPlayerFullscreen = playbackState.isFullscreen,
        videoResizeMode = playbackState.videoResizeMode,
        onPlaybackStateAction = viewModel::processPlaybackStateActions
    )
    val systemUIController = rememberSystemUiController()

    systemUIController.isNavigationBarVisible = !playerState.isFullscreen.value
    systemUIController.isStatusBarVisible = !playerState.isFullscreen.value

    LaunchedEffect(Unit) {
        viewModel.playbackEffects.onEach { effect ->
            when (effect) {
                PlaybackEvents.OnPlaybackRestart -> {
                    Napier.e("testing playbackEffects OnPlaybackRestart")
                    if (playerState.playerState.value == Player.STATE_IDLE) {
                        playerState.player.apply {
                            prepare()
                            playWhenReady = true
                        }
                    }
                }

                PlaybackEvents.OnChannelsUiToggle -> {
                    Napier.e("testing playbackEffects OnChannelsUiToggle")
                }

                PlaybackEvents.OnChannelInfoUiToggle -> {
                    Napier.e("testing playbackEffects OnChannelInfoUiToggle")
                }

                PlaybackEvents.OnEpgUiToggle -> {
                    viewModel.toggleEpgVisibility()
                }

                PlaybackEvents.OnFullScreenToggle -> {
                    playerState.toggleFullscreen()
                }

                PlaybackEvents.OnNextSelected -> {
                    playerState.next()
                }

                PlaybackEvents.OnPlaybackToggle -> {
                    playerState.togglePlayingState()
                }

                PlaybackEvents.OnPlayerUiToggle -> {
                    playerState.toggleControlUiState()
                }

                PlaybackEvents.OnPreviousSelected -> {
                    playerState.previous()
                }

                PlaybackEvents.OnVideoResizeToggle -> {
                    playerState.toggleVideoResizeMode()
                }

                PlaybackEvents.OnVolumeDown -> {
                    playerState.volumeDown()
                }

                PlaybackEvents.OnVolumeUp -> {
                    playerState.volumeUp()
                }
            }
        }.collect {}
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.TopCenter
    ) {
        if (playerState.isFullscreen.value) {
            PlayerView(
                modifier = Modifier,
                playerState = playerState,
                programs = playbackState.epgs,
                channelName = playbackState.currentChannel,
                onPlaybackAction = viewModel::processPlaybackActions
            )
        } else {
            TwoPane(
                first = {
                    PlayerView(
                        modifier = Modifier,
                        playerState = playerState,
                        programs = playbackState.epgs,
                        channelName = playbackState.currentChannel,
                        onPlaybackAction = viewModel::processPlaybackActions
                    )
                },
                second = {
                    PlayerOverlayEpgView(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface),
                        epgList = playbackState.epgs
                    )
                },
                strategy = when (windowSizeClass.widthSizeClass) {
                    Compact -> VerticalTwoPaneStrategy(MaterialTheme.dimens.fraction50)
                    Medium -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction60)
                    else -> HorizontalTwoPaneStrategy(MaterialTheme.dimens.fraction70)
                },
                displayFeatures = displayFeatures
            )
        }

        AnimatedVisibility(
            visible = playbackState.isEpgVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            PlayerOverlayEpg(
                controlState = playbackState,
                epgList = playbackState.epgs,
                onViewTap = viewModel::toggleEpgVisibility
            )
        }

        AnimatedVisibility(
            visible = !playbackState.isOnline,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
            ) {
                NoPlaybackView(
                    textRes = R.string.msg_no_internet_found,
                    iconRes = R.drawable.ic_no_network
                )
            }
        }

        AnimatedVisibility(
            visible = !playbackState.isMediaPlayable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(0.5f),
                contentAlignment = Alignment.Center
            ) {
                NoPlaybackView(
                    textRes = R.string.msg_no_playable_media_found,
                    iconRes = R.drawable.ic_sad_face
                )
            }
        }

        AnimatedVisibility(
            visible = playbackState.isBuffering,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoadingView()
        }

        /*        AnimatedVisibility(
                    visible = playbackState.isBrightnessChanging,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    val currentValue = remember(playbackState.brightnessValue) {
                        derivedStateOf {
                            playbackState.brightnessValue.calculateBrightnessProgress()
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        BrightnessProgressView(
                            value = { currentValue.value }
                        )
                    }
                }*/

        AnimatedVisibility(
            visible = playerState.isVolumeUiVisible.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(0.5f),
                contentAlignment = Alignment.Center
            ) {
                VolumeProgressView(
                    value = playerState.player.volume
                )
            }
        }
    }

    LaunchedEffect(playbackState.channels) {
        playerState.player.apply {
            setMediaItems(
                playbackState.channels.createMediaItems(),
                playbackState.mediaPosition,
                PLAYBACK_START_POSITION
            )
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(viewModel) {
        viewModel.initPlayBack(
            channelUrl = channelUrl,
            channelGroup = channelGroup
        )

        onDispose {
            playerState.player.stop()
        }
    }
}