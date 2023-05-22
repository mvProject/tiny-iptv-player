/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 19.05.23, 15:53
 *
 */

package com.mvproject.videoapp.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.getProperBrightnessIcon

@Composable
fun BrightnessProgressView(
    modifier: Modifier = Modifier,
    value: () -> Int,
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .width(MaterialTheme.dimens.size78)
                .height(MaterialTheme.dimens.size78)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(MaterialTheme.dimens.size48),
                imageVector = getProperBrightnessIcon(value()),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )

            Text(
                text = "${value()} %",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBrightnessProgressView() {
    VideoAppTheme() {
        BrightnessProgressView(value = { 7 })
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewBrightnessProgressView() {
    VideoAppTheme(darkTheme = true) {
        BrightnessProgressView(value = { 7 })
    }
}