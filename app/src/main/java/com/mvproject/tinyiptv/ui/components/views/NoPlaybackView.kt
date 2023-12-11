/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 14:44
 *
 */

package com.mvproject.tinyiptv.ui.components.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_50
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_80

@Composable
fun NoPlaybackView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    isFullScreen: Boolean = false,
    text: String = "NoPlaybackView",
    logo: Painter,
) {
    val fraction = remember(isFullScreen) {
        if (isFullScreen) WEIGHT_80 else WEIGHT_50
    }

    val alignment = remember(isFullScreen) {
        if (isFullScreen) Alignment.Center else Alignment.TopCenter
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = alignment
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(fraction)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.inverseSurface.copy(
                            alpha = MaterialTheme.dimens.alpha70
                        ),
                        shape = MaterialTheme.shapes.small
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = logo,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size96)
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        .padding(MaterialTheme.dimens.size12),
                    contentDescription = text
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewNoPlaybackView() {
    VideoAppTheme {
        NoPlaybackView(
            text = stringResource(id = R.string.msg_no_internet_found),
            logo = painterResource(id = R.drawable.ic_no_network)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DarkPreviewNoPlaybackView() {
    VideoAppTheme(darkTheme = true) {
        NoPlaybackView(
            text = stringResource(id = R.string.msg_no_internet_found),
            logo = painterResource(id = R.drawable.ic_no_network)
        )
    }
}


