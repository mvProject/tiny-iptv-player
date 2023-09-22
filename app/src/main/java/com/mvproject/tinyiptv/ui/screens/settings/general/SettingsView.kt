/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 31.08.23, 11:26
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
import androidx.compose.material3.ButtonDefaults
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
import com.mvproject.tinyiptv.ui.components.dialogs.OptionsDialog
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.screens.settings.general.action.SettingsAction
import com.mvproject.tinyiptv.ui.screens.settings.general.state.SettingsState
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

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
        },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
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

                FilledIconButton(
                    onClick = onNavigatePlaylistSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            TextButton(
                onClick = onNavigatePlayerSettings,
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

                FilledIconButton(
                    onClick = onNavigatePlayerSettings,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(id = R.string.option_update_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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

        OptionsDialog(
            isDialogOpen = isSelectInfoUpdateOpen,
            title = stringResource(id = R.string.pl_hint_update_period),
            selectedIndex = state.infoUpdatePeriod,
            items = UpdatePeriod.entries.map {
                stringResource(id = it.title)
            },
            onItemSelected = { index ->
                onSettingsAction(SettingsAction.SetInfoUpdatePeriod(index))
                isSelectInfoUpdateOpen.value = false
            }
        )

        OptionsDialog(
            isDialogOpen = isSelectEpgUpdateOpen,
            title = stringResource(id = R.string.pl_hint_update_period),
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