/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.epg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.data.PreviewTestData
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.components.views.DurationProgressView
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.COUNT_ZERO_FLOAT
import com.mvproject.videoapp.utils.AppConstants.PROGRESS_STATE_COMPLETE
import com.mvproject.videoapp.utils.TimeUtils.convertTimeToReadableFormat

@Composable
fun PlayerOverlayEpgItemView(
    modifier: Modifier = Modifier,
    program: EpgProgram,
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (isProgramProgressShow) {
            DurationProgressView(progress = program.programProgress)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerOverlayEpgItemView() {
    VideoAppTheme() {
        PlayerOverlayEpgItemView(program = PreviewTestData.testEpgProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerOverlayEpgItemView() {
    VideoAppTheme(darkTheme = true) {
        PlayerOverlayEpgItemView(program = PreviewTestData.testEpgProgram)
    }
}