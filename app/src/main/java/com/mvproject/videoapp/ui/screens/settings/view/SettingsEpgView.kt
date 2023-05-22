/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:25
 *
 */

package com.mvproject.videoapp.ui.screens.settings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.UpdatePeriod
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.screens.settings.actions.UpdateTypeAction
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsEpgViewModel
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsEpgView(
    updateState: SettingsEpgViewModel.UpdatePeriodState,
    onBackNavigate: () -> Unit = {},
    onUpdateTypeAction: (UpdateTypeAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_epg_settings_title),
                onBackClick = onBackNavigate,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(MaterialTheme.dimens.size8)
        ) {

            Text(
                text = stringResource(id = R.string.option_epg_info_update),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.infoUpdatePeriod,
                onItemSelected = { index, _ ->
                    onUpdateTypeAction(UpdateTypeAction.UpdateInfoPeriod(index))
                }
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )

            Text(
                text = stringResource(id = R.string.option_epg_main_update),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.mainUpdatePeriod,
                onItemSelected = { index, _ ->
                    onUpdateTypeAction(UpdateTypeAction.UpdateMainEpgPeriod(index))
                }
            )
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )

            Text(
                text = stringResource(id = R.string.option_epg_alter_update),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.alterUpdatePeriod,
                onItemSelected = { index, _ ->
                    onUpdateTypeAction(UpdateTypeAction.UpdateAlterEpgPeriod(index))
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsEpgView() {
    VideoAppTheme() {
        SettingsEpgView(
            updateState = SettingsEpgViewModel.UpdatePeriodState()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsEpgView() {
    VideoAppTheme(darkTheme = true) {
        SettingsEpgView(
            updateState = SettingsEpgViewModel.UpdatePeriodState()
        )
    }
}