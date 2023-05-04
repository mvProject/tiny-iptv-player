/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.components.toolbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvproject.videoapp.R
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun ToolbarMain(
    onSettingsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = MaterialTheme.dimens.size4,
                horizontal = MaterialTheme.dimens.size8
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 16.sp,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(start = MaterialTheme.dimens.size4),
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            modifier = Modifier.clickable {
                onSettingsClick()
            },
            imageVector = Icons.Outlined.Settings,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = null
        )
    }
}

@Composable
fun AppBarWithSettings(
    onSettingsClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 16.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.size4),
                color = MaterialTheme.colors.onPrimary
            )
        },
        actions = {
            IconButton(
                onClick = { onSettingsClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ToolbarMainPreview() {
    ToolbarMain()
}