/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.epg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.PreviewTestData
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun PlayerOverlayEpgView(
    modifier: Modifier = Modifier,
    epgList: List<EpgProgram>,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (epgList.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                text = stringResource(id = R.string.msg_no_epg_found),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

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
                    PlayerOverlayEpgItemView(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimens.size4),
                        program = epg,
                    )
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPlayerOverlayEpgView() {
    VideoAppTheme() {
        PlayerOverlayEpgView(epgList = PreviewTestData.testEpgPrograms)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewPlayerOverlayEpgView() {
    VideoAppTheme(darkTheme = true) {
        PlayerOverlayEpgView(epgList = PreviewTestData.testEpgPrograms)
    }
}