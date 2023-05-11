/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:25
 *
 */

package com.mvproject.videoapp.ui.screens.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.UpdatePeriod
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.screens.settings.viewmodel.EpgSettingsViewModel
import com.mvproject.videoapp.ui.screens.settings.viewmodel.UpdatePeriodState
import com.mvproject.videoapp.ui.screens.settings.viewmodel.UpdateTypeAction
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun SettingsEpgView(
    viewModel: EpgSettingsViewModel
) {
    val updateState by viewModel.state.collectAsState()
    SettingsEpgViewContent(
        updateState = updateState,
        onUpdateTypeAction = viewModel::processAction
    )
}

@Composable
fun SettingsEpgViewContent(
    updateState: UpdatePeriodState,
    onUpdateTypeAction: (UpdateTypeAction) -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_epg_settings_title),
                onBackClick = { navigator.pop() },
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            // var selectedInfoIndex by remember { mutableStateOf(updateState.infoUpdatePeriod) }
            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.infoUpdatePeriod,
                onItemSelected = { index, _ ->
                    //   selectedInfoIndex = index
                    onUpdateTypeAction(UpdateTypeAction.UpdateInfoPeriod(index))
                }
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )

            Text(
                text = stringResource(id = R.string.option_epg_main_update),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            //   var selectedMainIndex by remember { mutableStateOf(updateState.mainUpdatePeriod) }
            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.mainUpdatePeriod,
                onItemSelected = { index, _ ->
                    //selectedMainIndex = index
                    onUpdateTypeAction(UpdateTypeAction.UpdateMainEpgPeriod(index))
                }
            )
            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size12)
            )
            Text(
                text = stringResource(id = R.string.option_epg_alter_update),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(MaterialTheme.dimens.size8)
            )

            // var selectedAlterIndex by remember { mutableStateOf(updateState.alterUpdatePeriod) }
            LargeDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth(),
                label = stringResource(id = R.string.pl_hint_update_period),
                items = UpdatePeriod.values().map {
                    stringResource(id = it.title)
                },
                selectedIndex = updateState.alterUpdatePeriod,
                onItemSelected = { index, _ ->
                    // selectedAlterIndex = index
                    onUpdateTypeAction(UpdateTypeAction.UpdateAlterEpgPeriod(index))
                }
            )
        }
    }
}