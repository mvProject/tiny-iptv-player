/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.epg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.mvproject.videoapp.components.player.COUNT_ZERO_FLOAT
import com.mvproject.videoapp.components.player.PROGRESS_STATE_COMPLETE
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun PlayerEpgView(
    modifier: Modifier = Modifier,
    program: EpgProgram,
    textColor: Color = MaterialTheme.colors.onBackground,
    backColor: Color = MaterialTheme.colors.background,
    fontSize: TextUnit = MaterialTheme.dimens.font12
) {

    val isProgramEnded by remember {
        derivedStateOf {
            program.programProgress > PROGRESS_STATE_COMPLETE
        }
    }

    val isProgramProgressShow by remember {
        derivedStateOf {
            program.programProgress > COUNT_ZERO_FLOAT && program.programProgress <= PROGRESS_STATE_COMPLETE
        }
    }

    val cardAlpha =
        if (isProgramEnded)
            MaterialTheme.dimens.alpha50
        else MaterialTheme.dimens.alphaDefault

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)

    ) {
        val text = StringBuilder().apply {
            append(program.start.convertTimeToReadableFormat())
            append(" - ")
            append(program.title)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text.toString(),
            fontSize = fontSize,
            style = MaterialTheme.typography.h5,
            color = textColor,
            overflow = TextOverflow.Ellipsis
        )

        if (isProgramProgressShow) {
            LinearProgressIndicator(
                progress = program.programProgress,
                modifier = Modifier
                    .fillMaxWidth(),
                color = textColor,
                backgroundColor = backColor,
            )
        }
    }
}