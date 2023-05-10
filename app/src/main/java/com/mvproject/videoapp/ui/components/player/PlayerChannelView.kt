/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:28
 *
 */

package com.mvproject.videoapp.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.components.epg.PlayerEpgItemView
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import com.mvproject.videoapp.ui.theme.dimens
import io.github.aakira.napier.Napier

@Composable
fun PlayerChannelView(
    modifier: Modifier = Modifier,
    channelName: String,
    program: List<EpgProgram>,
    playerState: VideoViewViewModel.ControlUIState,
    onPlayerCommand: (command: PlayerCommands) -> Unit = {},
    onPlayerUICommand: (command: PlayerUICommands) -> Unit = {}
) {
    SideEffect {
        Napier.i("testing ProgramItem SideEffect")
    }

    Box(
        modifier = modifier.alpha(MaterialTheme.dimens.alpha70)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Color.DarkGray,
                    shape = RoundedCornerShape(
                        topStart = MaterialTheme.dimens.size16,
                        topEnd = MaterialTheme.dimens.size16
                    )
                )
                .align(Alignment.BottomCenter)
                .padding(MaterialTheme.dimens.size4),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = channelName,
                fontSize = MaterialTheme.dimens.font18,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .height(MaterialTheme.dimens.size2)
            )

            // todo user may select count
            program.take(1).forEach {
                PlayerEpgItemView(it)
            }

            Spacer(
                modifier = Modifier
                    .height(MaterialTheme.dimens.size2)
            )

            PlayerControls(
                modifier = Modifier
                    .fillMaxWidth(),
                playerState = playerState,
                onPlayerCommand = onPlayerCommand,
                onPlayerUICommand = onPlayerUICommand
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramViewPreview() {
    PlayerChannelView(
        program = emptyList(),
        channelName = "Current Channel",
        playerState = VideoViewViewModel.ControlUIState(50, 15)
    )
}

@Preview(showBackground = true)
@Composable
fun ProgramViewPreviewAlter() {
    PlayerChannelView(
        program = emptyList(),
        channelName = "Current Channel",
        playerState = VideoViewViewModel.ControlUIState(50, 15)
    )
}

fun calculateProgramProgress(startTime: Long, endTime: Long): Float {
    var progressValue = 0f
    val currTime = System.currentTimeMillis()
    if (currTime > startTime) {
        val endValue = (endTime - startTime).toInt()
        val spendValue = (currTime - startTime).toDouble()
        progressValue = (spendValue / endValue).toFloat()
    }
    return progressValue
}







