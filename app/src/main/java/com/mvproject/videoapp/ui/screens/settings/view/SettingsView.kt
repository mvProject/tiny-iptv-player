/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:25
 *
 */

package com.mvproject.videoapp.ui.screens.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.R
import com.mvproject.videoapp.navigation.SettingsScreenEpgRoute
import com.mvproject.videoapp.navigation.SettingsScreenPlaylistRoute
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.theme.dimens

@Composable
fun SettingsView() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_settings_title),
                onBackClick = { navigator.pop() },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
        ) {
            Text(
                text = stringResource(id = R.string.scr_playlist_settings_title),
                modifier = Modifier.clickable {
                    navigator.push(SettingsScreenPlaylistRoute())
                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Text(
                text = "Playlists Update",
                modifier = Modifier.clickable {

                }
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.size12))

            Text(
                text = stringResource(id = R.string.scr_epg_settings_title),
                modifier = Modifier.clickable {
                    navigator.push(SettingsScreenEpgRoute())
                }
            )
        }
    }
}