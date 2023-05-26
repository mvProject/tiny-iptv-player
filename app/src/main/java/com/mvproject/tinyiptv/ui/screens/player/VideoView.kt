/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:26
 *
 */

package com.mvproject.tinyiptv.ui.screens.player

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mvproject.tinyiptv.ui.components.epg.PlayerOverlayEpg
import com.mvproject.tinyiptv.ui.components.epg.PlayerOverlayEpgView
import com.mvproject.tinyiptv.ui.components.player.PlayerView
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.calculateProperBrightnessValue
import com.mvproject.tinyiptv.utils.findActivity
import com.mvproject.tinyiptv.utils.setBrightness

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun VideoView(
    viewModel: VideoViewViewModel,
    channelId: String,
    channelGroup: String
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val controlState by viewModel.playerUIState.collectAsState()
    val epgState by viewModel.currentEpg.collectAsState()
    val systemUIController = rememberSystemUiController()

    val windowSizeClass = calculateWindowSizeClass(activity)
    val displayFeatures = calculateDisplayFeatures(activity)

    val targetBrightness by remember {
        derivedStateOf {
            calculateProperBrightnessValue(controlState.brightnessValue)
        }
    }

    activity.setBrightness(targetBrightness)

    systemUIController.isNavigationBarVisible = !controlState.isFullscreen
    systemUIController.isStatusBarVisible = !controlState.isFullscreen

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
            .systemBarsPadding()
    ) {
        if (controlState.isFullscreen) {
            PlayerView(
                modifier = Modifier,
                playerState = controlState,
                player = viewModel.player,
                programs = epgState,
                onViewSettingsAction = viewModel::processViewSettingsChange,
                onPlayerCommand = viewModel::processPlayerCommand,
                onPlayerUICommand = viewModel::processPlayerUICommand
            )
        } else {
            TwoPane(
                first = {
                    PlayerView(
                        modifier = Modifier,
                        playerState = controlState,
                        player = viewModel.player,
                        programs = epgState,
                        onViewSettingsAction = viewModel::processViewSettingsChange,
                        onPlayerCommand = viewModel::processPlayerCommand,
                        onPlayerUICommand = viewModel::processPlayerUICommand
                    )
                },
                second = {
                    PlayerOverlayEpgView(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface),
                        epgList = epgState
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

        if (controlState.isEpgVisible) {
            PlayerOverlayEpg(
                controlState = controlState,
                epgList = epgState,
                onViewTap = viewModel::toggleEpgVisibility
            )
        }
    }

    DisposableEffect(viewModel) {
        viewModel.initPlayBack(
            channelId = channelId,
            channelGroup = channelGroup
        )
        onDispose {
            viewModel.cleanPlayback()
        }
    }
}