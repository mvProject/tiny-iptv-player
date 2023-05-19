/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 18.05.23, 19:09
 *
 */

package com.mvproject.videoapp.ui.components.toolbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBackNav(
    appBarTitle: String,
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = appBarTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        navigationIcon = {
            FilledIconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(MaterialTheme.dimens.size8),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewAppAppBarWithBackNav() {
    VideoAppTheme() {
        AppBarWithBackNav(
            appBarTitle = stringResource(id = R.string.app_name),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkAppBarWithBackNav() {
    VideoAppTheme(darkTheme = true) {
        AppBarWithBackNav(
            appBarTitle = stringResource(id = R.string.app_name)
        )
    }
}