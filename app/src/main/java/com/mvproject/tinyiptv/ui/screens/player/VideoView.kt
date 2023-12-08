/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 14:47
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.components.epg.PlayerEpgContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayChannelInfo
import com.mvproject.tinyiptv.ui.components.overlay.OverlayChannels
import com.mvproject.tinyiptv.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayEpg
import com.mvproject.tinyiptv.ui.components.player.PlayerChannelView
import com.mvproject.tinyiptv.ui.components.player.PlayerView
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.components.views.NoPlaybackView
import com.mvproject.tinyiptv.ui.components.views.VolumeProgressView
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.findActivity

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun VideoView(
    viewModel: VideoViewViewModel,
    channelName: String,
    channelGroup: String
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val windowSizeClass = calculateWindowSizeClass(activity)
    val displayFeatures = calculateDisplayFeatures(activity)

    val videoViewState by viewModel.videoViewState.collectAsState()

    val fraction = remember(videoViewState.isFullscreen) {
        if (videoViewState.isFullscreen) 1f else 0.5f
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim),
        contentAlignment = Alignment.TopCenter
    ) {
        if (videoViewState.isFullscreen) {
            PlayerView(
                modifier = Modifier,
                videoViewState = videoViewState,
                onPlaybackAction = viewModel::processPlaybackActions,
                onPlaybackStateAction = viewModel::processPlaybackStateActions
            ) {
                AnimatedVisibility(
                    visible = videoViewState.isControlUiVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PlayerChannelView(
                        modifier = Modifier.fillMaxSize(),
                        currentChannel = videoViewState.currentChannel,
                        isPlaying = videoViewState.isPlaying,
                        isFullScreen = true,
                        onPlaybackAction = viewModel::processPlaybackActions
                    )
                }
            }
        } else {
            TwoPane(
                first = {
                    PlayerView(
                        modifier = Modifier,
                        videoViewState = videoViewState,
                        onPlaybackAction = viewModel::processPlaybackActions,
                        onPlaybackStateAction = viewModel::processPlaybackStateActions
                    ) {
                        AnimatedVisibility(
                            visible = videoViewState.isControlUiVisible,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            PlayerChannelView(
                                modifier = Modifier.fillMaxSize(),
                                currentChannel = videoViewState.currentChannel,
                                isPlaying = videoViewState.isPlaying,
                                isFullScreen = false,
                                onPlaybackAction = viewModel::processPlaybackActions
                            )
                        }
                    }
                },
                second = {
                    PlayerEpgContent(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        epgList = videoViewState.currentChannel.channelEpg
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

        OverlayContent(
            isVisible = videoViewState.isEpgVisible,
            onViewTap = viewModel::toggleEpgVisibility
        ) {
            OverlayEpg(
                isFullScreen = videoViewState.isFullscreen,
                currentChannel = videoViewState.currentChannel
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelsVisible,
            onViewTap = viewModel::toggleChannelsVisibility,
            contentAlpha = MaterialTheme.dimens.alpha90
        ) {
            OverlayChannels(
                isFullScreen = videoViewState.isFullscreen,
                channels = videoViewState.channels,
                current = videoViewState.mediaPosition,
                group = videoViewState.channelGroup,
                onChannelSelect = viewModel::switchToChannel
            )
        }

        OverlayContent(
            isVisible = videoViewState.isChannelInfoVisible,
            onViewTap = viewModel::toggleChannelInfoVisibility
        ) {
            OverlayChannelInfo(
                isFullScreen = videoViewState.isFullscreen,
                currentChannel = videoViewState.currentChannel
            )
        }

        VolumeProgressView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isVolumeUiVisible,
            value = videoViewState.currentVolume
        )

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = !videoViewState.isOnline,
            isFullScreen = videoViewState.isFullscreen,
            text = stringResource(id = R.string.msg_no_internet_found),
            logo = painterResource(id = R.drawable.ic_no_network)
        )

        NoPlaybackView(
            modifier = Modifier.fillMaxSize(),
            isVisible = !videoViewState.isMediaPlayable,
            isFullScreen = videoViewState.isFullscreen,
            text = stringResource(id = R.string.msg_no_playable_media_found),
            logo = painterResource(id = R.drawable.ic_sad_face)
        )

        LoadingView(
            modifier = Modifier.fillMaxSize(fraction),
            isVisible = videoViewState.isBuffering
        )
    }

    DisposableEffect(viewModel) {
        viewModel.initPlayBack(
            channelName = channelName,
            channelGroup = channelGroup
        )

        onDispose {}
    }
}