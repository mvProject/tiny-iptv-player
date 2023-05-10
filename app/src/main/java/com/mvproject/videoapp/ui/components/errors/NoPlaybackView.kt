/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.videoapp.ui.components.errors

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun NoPlaybackView(
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.DarkGray
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = iconRes),
            tint = Color.LightGray,
            contentDescription = null
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.size8)
        )

        Text(
            text = stringResource(id = textRes),
            fontSize = 16.sp,
            color = Color.LightGray,
            overflow = TextOverflow.Ellipsis
        )
    }
}


