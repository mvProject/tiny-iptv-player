/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 14:33
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.getProperVolumeIcon

@Composable
fun VolumeProgressView(
    modifier: Modifier = Modifier,
    value: Float,
    isVisible: Boolean = false
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            val volumeDisplay = (value * 100).toInt()

            Column(
                modifier = Modifier
                    .width(MaterialTheme.dimens.size78)
                    .height(MaterialTheme.dimens.size78)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    modifier = Modifier.size(MaterialTheme.dimens.size48),
                    imageVector = getProperVolumeIcon(volumeDisplay),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )

                Text(
                    text = "$volumeDisplay %",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PreviewVolumeProgressView() {
    VideoAppTheme() {
        VolumeProgressView(value = 0.4f)
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewVolumeProgressView() {
    VideoAppTheme(darkTheme = true) {
        VolumeProgressView(value = 0.8f)
    }
}