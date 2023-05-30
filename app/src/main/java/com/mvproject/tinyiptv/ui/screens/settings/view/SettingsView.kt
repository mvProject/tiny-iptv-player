/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:25
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.screens.settings.actions.SettingsAction
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    onSettingsAction: (SettingsAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_settings_title),
                onBackClick = { onSettingsAction(SettingsAction.NavigateBack) },
            )
        },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
        ) {

            TextButton(
                onClick = { onSettingsAction(SettingsAction.NavigatePlaylistSettings) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.scr_playlist_settings_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Rounded.ArrowCircleRight,
                    contentDescription = stringResource(id = R.string.scr_playlist_settings_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            TextButton(
                onClick = { onSettingsAction(SettingsAction.NavigateEpgSettings) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.scr_epg_settings_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Rounded.ArrowCircleRight,
                    contentDescription = stringResource(id = R.string.scr_epg_settings_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            TextButton(
                onClick = { onSettingsAction(SettingsAction.NavigatePlayerSettings) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.scr_player_settings_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Rounded.ArrowCircleRight,
                    contentDescription = stringResource(id = R.string.scr_player_settings_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            // todo set enabled and route update after implementation
            TextButton(
                onClick = { onSettingsAction(SettingsAction.NavigateAppSettings) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                enabled = false
            ) {
                Text(
                    text = stringResource(id = R.string.scr_app_settings_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Rounded.ArrowCircleRight,
                    contentDescription = stringResource(id = R.string.scr_app_settings_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsView() {
    VideoAppTheme() {
        SettingsView()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsView() {
    VideoAppTheme(darkTheme = true) {
        SettingsView()
    }
}