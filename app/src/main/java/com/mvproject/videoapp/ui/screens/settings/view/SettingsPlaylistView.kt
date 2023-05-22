/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:25
 *
 */

package com.mvproject.videoapp.ui.screens.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.PreviewTestData.testPlaylists
import com.mvproject.videoapp.ui.components.playlist.PlaylistItemView
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.components.views.NoItemsView
import com.mvproject.videoapp.ui.screens.settings.actions.SettingsPlaylistAction
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsPlaylistViewModel
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPlaylistView(
    dataState: SettingsPlaylistViewModel.PlaylistSettingsState,
    onPlaylistAction: (SettingsPlaylistAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.scr_playlist_settings_title),
                onBackClick = { onPlaylistAction(SettingsPlaylistAction.NavigateBack) },
            )
        },

        bottomBar = {
            ElevatedButton(
                onClick = { onPlaylistAction(SettingsPlaylistAction.NewPlaylist) },
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(id = R.string.pl_btn_add_new),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4),
                contentPadding = PaddingValues(MaterialTheme.dimens.size8)
            ) {
                items(
                    dataState.playlists,
                    key = { it.id }
                ) { item ->
                    PlaylistItemView(
                        modifier = Modifier.fillMaxSize(),
                        item = item,
                        onPlaylistAction = onPlaylistAction
                    )
                }
            }

            if (dataState.dataIsEmpty) {
                NoItemsView(
                    modifier = Modifier.fillMaxSize(),
                    navigateTitle = stringResource(id = R.string.pl_msg_no_playlist)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsPlaylistView() {
    VideoAppTheme() {
        SettingsPlaylistView(
            SettingsPlaylistViewModel.PlaylistSettingsState(
                playlists = testPlaylists
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkSettingsPlaylistView() {
    VideoAppTheme(darkTheme = true) {
        SettingsPlaylistView(
            SettingsPlaylistViewModel.PlaylistSettingsState(
                playlists = testPlaylists
            )
        )
    }
}
