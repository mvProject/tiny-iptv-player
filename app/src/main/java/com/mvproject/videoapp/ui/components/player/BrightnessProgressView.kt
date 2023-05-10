/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 14:53
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.ui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessHigh
import androidx.compose.material.icons.rounded.BrightnessLow
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.aakira.napier.Napier

@Composable
fun BrightnessProgressView(
    modifier: Modifier = Modifier,
    value: () -> Int,
) {
    SideEffect {
        Napier.i("testing UpdateBrightnessProgressView SideEffect")
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .width(78.dp)
                .height(78.dp)
                .background(
                    Color.DarkGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.Center)
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = brightnessIcon(value()),
                tint = Color.LightGray,
                contentDescription = null
            )

            Text(
                text = "${value()} %",
                fontSize = 16.sp,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateBrightnessProgressViewFirst() {
    BrightnessProgressView(value = { 7 })
}

@Preview(showBackground = true)
@Composable
fun UpdateBrightnessProgressViewSecond() {
    BrightnessProgressView(value = { 18 })
}

@Preview(showBackground = true)
@Composable
fun UpdateBrightnessProgressViewThird() {
    BrightnessProgressView(value = { 28 })
}

fun brightnessIcon(value: Int) = when {
    value < 35 -> Icons.Rounded.BrightnessLow
    value > 65 -> Icons.Rounded.BrightnessHigh
    else -> Icons.Rounded.BrightnessMedium
}