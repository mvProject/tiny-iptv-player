/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.navigation.SettingsScreenEpgRoute
import com.mvproject.videoapp.navigation.SettingsScreenPlaylistRoute
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
                appBarTitle = "Settings",
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
                text = "Playlists Settings",
                modifier = Modifier.clickable {
                    navigator.push(SettingsScreenPlaylistRoute())
                }
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Playlists Update",
                modifier = Modifier.clickable {

                }
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Epg Settings",
                modifier = Modifier.clickable {
                    navigator.push(SettingsScreenEpgRoute())
                }
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Epg Update",
                modifier = Modifier.clickable {

                }
            )
        }
    }
}