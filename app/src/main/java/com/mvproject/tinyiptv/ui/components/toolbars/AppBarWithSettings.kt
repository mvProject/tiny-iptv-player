/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:19
 *
 */

package com.mvproject.tinyiptv.ui.components.toolbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithSettings(
    onSettingsClicked: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        actions = {
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarWithSettingsPreview() {
    VideoAppTheme() {
        AppBarWithSettings()
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarWithSettingsDarkPreview() {
    VideoAppTheme(darkTheme = true) {
        AppBarWithSettings()
    }
}