/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 12.05.23, 19:31
 *
 */

package com.mvproject.videoapp.ui.components.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.ui.theme.VideoAppTheme

@Composable
fun DurationProgressView(
    modifier: Modifier = Modifier.fillMaxWidth(),
    progress: Float,
    trackColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    backColor: Color = MaterialTheme.colorScheme.tertiary,
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        trackColor = trackColor,
        color = backColor,
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewDurationProgressView() {
    VideoAppTheme() {
        DurationProgressView(progress = 0.5f)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewDurationProgressView() {
    VideoAppTheme(darkTheme = true) {
        DurationProgressView(progress = 0.5f)
    }
}