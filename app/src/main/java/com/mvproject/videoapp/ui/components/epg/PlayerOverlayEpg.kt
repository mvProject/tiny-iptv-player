/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 20:20
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants

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

            PlayerOverlayEpgView(
                modifier = Modifier.background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(
                        bottomStart = MaterialTheme.dimens.size8,
                        bottomEnd = MaterialTheme.dimens.size8
                    )
                ),
                textColor = MaterialTheme.colors.primary,
                backColor = MaterialTheme.colors.onPrimary,
                epgList = epgList
            )
        }
    }
}