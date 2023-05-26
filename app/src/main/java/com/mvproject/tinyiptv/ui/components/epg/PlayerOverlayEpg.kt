/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.components.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.data.PreviewTestData
import com.mvproject.tinyiptv.data.models.epg.EpgProgram
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants

@Composable
fun PlayerOverlayEpg(
    controlState: VideoViewViewModel.ControlUIState,
    epgList: List<EpgProgram>,
    onViewTap: () -> Unit = {}
) {
    val viewWidth by remember {
        derivedStateOf {
            if (controlState.isFullscreen) AppConstants.WEIGHT_50 else AppConstants.WEIGHT_80
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
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(
                            topStart = MaterialTheme.dimens.size8,
                            topEnd = MaterialTheme.dimens.size8
                        )
                    )
                    .padding(all = MaterialTheme.dimens.size8),
                text = controlState.currentChannel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            PlayerOverlayEpgView(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = RoundedCornerShape(
                        bottomStart = MaterialTheme.dimens.size8,
                        bottomEnd = MaterialTheme.dimens.size8
                    )
                ),
                epgList = epgList
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerOverlayEpg() {
    VideoAppTheme() {
        PlayerOverlayEpg(
            controlState = VideoViewViewModel.ControlUIState(
                brightnessValue = 1,
                volumeValue = 1
            ),
            epgList = PreviewTestData.testEpgPrograms
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerOverlayEpg() {
    VideoAppTheme(darkTheme = true) {
        PlayerOverlayEpg(
            controlState = VideoViewViewModel.ControlUIState(
                brightnessValue = 1,
                volumeValue = 1
            ),
            epgList = PreviewTestData.testEpgPrograms
        )
    }
}