/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
fun PlayerEpgItemView(
    epgProgram: EpgProgram
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = epgProgram.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.size2))

        val isProgramProgressShow by remember {
            derivedStateOf {
                epgProgram.programProgress > COUNT_ZERO_FLOAT && epgProgram.programProgress <= PROGRESS_STATE_COMPLETE
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
                    modifier = Modifier.wrapContentWidth(),
                    text = epgProgram.start.convertTimeToReadableFormat(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                DurationProgressView(
                    modifier = Modifier.weight(6f),
                    progress = epgProgram.programProgress
                )

                Text(
                    modifier = Modifier.wrapContentWidth(),
                    text = epgProgram.stop.convertTimeToReadableFormat(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerEpgItemView() {
    VideoAppTheme() {
        PlayerEpgItemView(epgProgram = PreviewTestData.testEpgProgram)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerEpgItemView() {
    VideoAppTheme(darkTheme = true) {
        PlayerEpgItemView(epgProgram = PreviewTestData.testEpgProgram)
    }
}