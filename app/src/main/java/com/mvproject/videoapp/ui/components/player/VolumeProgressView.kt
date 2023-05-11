/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.videoapp.utils.getProperVolumeIcon
import io.github.aakira.napier.Napier

@Composable
fun VolumeProgressView(
    modifier: Modifier = Modifier,
    value: () -> Int,
) {
    SideEffect {
        Napier.i("testing UpdateVolumeProgressView SideEffect")
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
                imageVector = getProperVolumeIcon(value()),
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
fun UpdateVolumeProgressViewFirst() {
    VolumeProgressView(value = { 7 })
}

@Preview(showBackground = true)
@Composable
fun UpdateVolumeProgressViewSecond() {
    VolumeProgressView(value = { 18 })
}

@Preview(showBackground = true)
@Composable
fun UpdateVolumeProgressViewThird() {
    VolumeProgressView(value = { 28 })
}