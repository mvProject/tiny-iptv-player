/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 13:16
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.enums.ResizeMode
import com.mvproject.tinyiptv.ui.components.menu.LargeDropdownMenu
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.screens.settings.actions.SettingsPlayerAction
import com.mvproject.tinyiptv.ui.screens.settings.viewmodel.SettingsPlayerViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun SettingsPlayerView(
    state: SettingsPlayerViewModel.PlayerSettingsState,
    onSettingsPlayerAction: (SettingsPlayerAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_player_settings_title),
                onBackClick = { onSettingsPlayerAction(SettingsPlayerAction.NavigateBack) },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size12)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.option_default_fullscreen_mode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = state.isFullscreenEnabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.tertiaryContainer,
                        checkedTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    onCheckedChange = { state ->
                        onSettingsPlayerAction(
                            SettingsPlayerAction.ChangeFullScreenMode(
                                state
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

            Text(
                text = stringResource(id = R.string.option_default_resize_mode),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                items = ResizeMode.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = state.resizeMode,
                onItemSelected = { index, _ ->
                    onSettingsPlayerAction(SettingsPlayerAction.ChangeResizeModePeriod(index))
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsPlayerView() {
    VideoAppTheme {
        SettingsPlayerView(
            state = SettingsPlayerViewModel.PlayerSettingsState()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsPlayerView() {
    VideoAppTheme(darkTheme = true) {
        SettingsPlayerView(
            state = SettingsPlayerViewModel.PlayerSettingsState()
        )
    }
}
