/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 17:16
 *
 */

package com.mvproject.tinyiptv.ui.screens.settings.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.ui.components.OptionSelector
import com.mvproject.tinyiptv.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayOptionsMenu
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.screens.settings.general.action.SettingsAction
import com.mvproject.tinyiptv.ui.screens.settings.general.state.SettingsState
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_1

@Composable
fun SettingsView(
    state: SettingsState,
    onSettingsAction: (SettingsAction) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigatePlaylistSettings: () -> Unit = {},
    onNavigatePlayerSettings: () -> Unit = {},

    ) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_settings_title),
                onBackClick = onNavigateBack,
            )
        }
    ) { paddingValues ->

        val isSelectInfoUpdateOpen = remember { mutableStateOf(false) }
        val isSelectEpgUpdateOpen = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
        ) {

            TextButton(
                onClick = onNavigatePlaylistSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.scr_playlist_settings_title),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(WEIGHT_1))

                FilledIconButton(
                    onClick = onNavigatePlaylistSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = stringResource(id = R.string.scr_playlist_settings_title)
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            TextButton(
                onClick = onNavigatePlayerSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.scr_player_settings_title),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(WEIGHT_1))

                FilledIconButton(
                    onClick = onNavigatePlayerSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = stringResource(id = R.string.scr_player_settings_title)
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                horizontalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.dimens.size8
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(WEIGHT_1),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = stringResource(id = R.string.option_update_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(
                    modifier = Modifier.weight(WEIGHT_1),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )

            OptionSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                title = stringResource(id = R.string.option_update_epg_info),
                selectedItem = stringResource(
                    id = UpdatePeriod.entries[state.infoUpdatePeriod].title
                ),
                isExpanded = isSelectInfoUpdateOpen.value,
                onClick = {
                    isSelectInfoUpdateOpen.value = true
                }
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )

            OptionSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.size8),
                title = stringResource(id = R.string.option_update_epg_data),
                selectedItem = stringResource(
                    id = UpdatePeriod.entries[state.epgUpdatePeriod].title
                ),
                isExpanded = isSelectEpgUpdateOpen.value,
                onClick = {
                    isSelectEpgUpdateOpen.value = true
                }
            )
        }

        OverlayContent(
            isVisible = isSelectInfoUpdateOpen.value,
            contentAlpha = MaterialTheme.dimens.alpha90,
            onViewTap = { isSelectInfoUpdateOpen.value = false }
        ) {
            OverlayOptionsMenu(
                title = stringResource(id = R.string.hint_update_period),
                selectedIndex = state.infoUpdatePeriod,
                items = UpdatePeriod.entries.map {
                    stringResource(id = it.title)
                },
                onItemSelected = { index ->
                    onSettingsAction(SettingsAction.SetInfoUpdatePeriod(index))
                    isSelectInfoUpdateOpen.value = false
                }
            )
        }

        OverlayContent(
            isVisible = isSelectEpgUpdateOpen.value,
            contentAlpha = MaterialTheme.dimens.alpha90,
            onViewTap = { isSelectEpgUpdateOpen.value = false }
        ) {
            OverlayOptionsMenu(
                title = stringResource(id = R.string.hint_update_period),
                selectedIndex = state.epgUpdatePeriod,
                items = UpdatePeriod.entries.map {
                    stringResource(id = it.title)
                },
                onItemSelected = { index ->
                    onSettingsAction(SettingsAction.SetEpgUpdatePeriod(index))
                    isSelectEpgUpdateOpen.value = false
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsView() {
    VideoAppTheme() {
        SettingsView(state = SettingsState())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsView() {
    VideoAppTheme(darkTheme = true) {
        SettingsView(state = SettingsState())
    }
}