/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mvproject.videoapp.R
import com.mvproject.videoapp.components.epg.PlayerEpgView
import com.mvproject.videoapp.components.modifiers.defaultPlayerHorizontalGestures
import com.mvproject.videoapp.components.modifiers.defaultPlayerVerticalGestures
import com.mvproject.videoapp.components.player.PlayControls
import com.mvproject.videoapp.components.player.VideoPlayerView
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.WEIGHT_50
import com.mvproject.videoapp.utils.AppConstants.WEIGHT_80
import com.mvproject.videoapp.utils.calculateProperBrightnessValue
import com.mvproject.videoapp.utils.findActivity
import com.mvproject.videoapp.utils.setBrightness
import com.mvproject.videoapp.utils.setOrientation
import io.github.aakira.napier.Napier

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

    SideEffect {
        Napier.e("VideoView SideEffect")
    }

    activity.setOrientation(controlState.isFullscreen)
    activity.setBrightness(calculateProperBrightnessValue(controlState.brightnessValue))

    systemUIController.isNavigationBarVisible = !controlState.isFullscreen
    systemUIController.isStatusBarVisible = !controlState.isFullscreen

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoPlayerView(
            modifier = Modifier
                .background(Color.Black)
                .adaptiveSize(controlState.isFullscreen)
                .defaultPlayerHorizontalGestures(onPlayerCommand = viewModel::processPlayerCommand)
                .defaultPlayerVerticalGestures(onAction = viewModel::processViewSettingsChange),
            playerState = controlState,
            player = viewModel.player,
            onPlayerUICommand = viewModel::processPlayerUICommand
        ) {

            PlayControls(
                modifier = Modifier.fillMaxSize(),
                playerState = controlState,
                programs = epgState,
                onPlayerCommand = viewModel::processPlayerCommand,
                onPlayerUICommand = viewModel::processPlayerUICommand
            )
        }

        if (controlState.isEpgVisible) {
            PlayerOverlayEpg(
                controlState = controlState,
                epgList = viewModel.channelsEpgs,
                onViewTap = viewModel::toggleEpgVisibility
            )
        }
    }

    DisposableEffect(viewModel) {
        Napier.e("testing1 VideoView DisposableEffect")
        viewModel.initPlayBack(
            channelId = channelId,
            channelGroup = channelGroup
        )
        onDispose {
            viewModel.cleanPlayback()
            Napier.e("testing1 VideoView onDispose")
        }
    }
}

@Composable
fun PlayerOverlayEpg(
    controlState: VideoViewViewModel.ControlUIState,
    epgList: List<EpgProgram>,
    onViewTap: () -> Unit = {}
) {
    val viewWidth by remember {
        derivedStateOf {
            if (controlState.isFullscreen) WEIGHT_50 else WEIGHT_80
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .alpha(MaterialTheme.dimens.alpha60)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { onViewTap() }
            )
        }) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight(MaterialTheme.dimens.fraction90)
                .fillMaxWidth(viewWidth)
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(MaterialTheme.dimens.size8)
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(
                            topStart = MaterialTheme.dimens.size8,
                            topEnd = MaterialTheme.dimens.size8
                        )
                    )
                    .padding(all = MaterialTheme.dimens.size8),
                text = controlState.currentChannel,
                fontSize = MaterialTheme.dimens.font18,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                overflow = TextOverflow.Ellipsis,
            )

            PlayerOverlayEpgContent(epgList = epgList)
        }
    }
}

@Composable
fun PlayerOverlayEpgContent(epgList: List<EpgProgram>) {
    if (epgList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(
                        bottomStart = MaterialTheme.dimens.size8,
                        bottomEnd = MaterialTheme.dimens.size8
                    )
                )

        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = stringResource(id = R.string.msg_no_epg_found),
                fontSize = MaterialTheme.dimens.font16,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size8),
            contentPadding = PaddingValues(
                vertical = MaterialTheme.dimens.size4,
                horizontal = MaterialTheme.dimens.size2
            ),
            content = {
                items(
                    items = epgList,
                    key = { (it.start.toString() + it.title) }
                ) { epg ->
                    PlayerEpgView(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimens.size4),
                        program = epg,
                        textColor = MaterialTheme.colors.primary,
                        backColor = MaterialTheme.colors.onPrimary,
                        fontSize = MaterialTheme.dimens.font14
                    )
                }
            }
        )
    }
}


private fun Modifier.adaptiveSize(
    isFullscreen: Boolean
): Modifier {
    return if (isFullscreen) {
        fillMaxSize()
    } else {
        fillMaxWidth().fillMaxHeight(WEIGHT_50)
    }
}