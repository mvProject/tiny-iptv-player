/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.videoapp.components.player.COUNT_ZERO_FLOAT
import com.mvproject.videoapp.components.player.PROGRESS_STATE_COMPLETE
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun ChannelEpgInfo(
    program: EpgProgram
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = program.title,
            fontSize = 14.sp,
            color = Color.LightGray,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = 2.dp)
        )

        val isProgramProgressShow by remember {
            derivedStateOf {
                program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE
            }
        }

        if (isProgramProgressShow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimens.size4
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth(),
                    text = program.start.convertTimeToReadableFormat(),
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                LinearProgressIndicator(
                    progress = program.programProgress,
                    modifier = Modifier
                        .weight(6f),
                    color = Color.Blue,
                    backgroundColor = Color.Green,
                )

                Text(
                    modifier = Modifier
                        .wrapContentWidth(),
                    text = program.stop.convertTimeToReadableFormat(),
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }


    }
}