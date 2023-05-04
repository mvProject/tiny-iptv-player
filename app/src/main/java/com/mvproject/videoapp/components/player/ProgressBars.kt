/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.player

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.videoapp.components.epg.ChannelEpgInfo
import com.mvproject.videoapp.data.enums.player.PlayerCommands
import com.mvproject.videoapp.data.enums.player.PlayerUICommands
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.presentation.player.VideoViewViewModel
import com.mvproject.videoapp.ui.theme.dimens
import io.github.aakira.napier.Napier

@Composable
fun ProgramItem(
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
        modifier = modifier.alpha(0.7f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Color.DarkGray,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .align(Alignment.BottomCenter)
                .padding(MaterialTheme.dimens.size4),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = channelName,
                fontSize = 18.sp,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .height(MaterialTheme.dimens.size2)
            )

            program.forEach {
                ChannelEpgInfo(it)
            }

            Spacer(
                modifier = Modifier
                    .height(MaterialTheme.dimens.size2)
            )

            PlayerControl(
                modifier = Modifier
                    .fillMaxWidth(),
                playerState = playerState,
                onPlayerCommand = onPlayerCommand,
                onPlayerUICommand = onPlayerUICommand
            )
        }
    }
}

data class Program(
    val currentProgram: String,
    val nextProgram: String,
    val dateTimeStart: Long,
    val dateTimeEnd: Long,
) {
    val programProgress
        get() = calculateProgramProgress(
            startTime = dateTimeStart,
            endTime = dateTimeEnd
        )
}


val programTest
    get() : Program {
        val diff = 5400000L
        val start = System.currentTimeMillis() - 1200000L
        val end = start + diff
        return Program(
            currentProgram = "CurrentProgram",
            nextProgram = "Next Program",
            dateTimeStart = start,
            dateTimeEnd = end
        )
    }
const val ROTATION_STATE_UP = 180f
const val ROTATION_STATE_DOWN = 0f
const val ANIM_DURATION_300 = 300
const val PROGRESS_STATE_COMPLETE = 1f
const val COUNT_ZERO_FLOAT = 0f


@Preview(showBackground = true)
@Composable
fun ProgramViewPreview() {
    ProgramItem(
        program = emptyList(),
        channelName = "Current Channel",
        playerState = VideoViewViewModel.ControlUIState(50, 15)
    )
}

@Preview(showBackground = true)
@Composable
fun ProgramViewPreviewAlter() {
    ProgramItem(
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







