/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 11:30
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.PreviewTestData.testChannelsGroups
import com.mvproject.tinyiptv.ui.components.menu.LargeDropdownMenu
import com.mvproject.tinyiptv.ui.components.playlist.PlaylistGroupItemView
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithSettings
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.components.views.NoItemsView
import com.mvproject.tinyiptv.ui.screens.playlist.actions.PlaylistAction
import com.mvproject.tinyiptv.ui.screens.playlist.viewmodel.PlaylistDataViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens

@Composable
fun PlaylistDataView(
    dataState: PlaylistDataViewModel.PlaylistDataState,
    onPlaylistAction: (PlaylistAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            AppBarWithSettings() {
                onPlaylistAction(PlaylistAction.NavigateToSettings)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size8),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (dataState.isPlaylistSelectorVisible) {
                    var selectedIndex by remember {
                        mutableStateOf(dataState.playlistSelectedIndex)
                    }
                    LargeDropdownMenu(modifier = Modifier.fillMaxWidth(),
                        title = stringResource(id = R.string.pl_hint_current_playlist),
                        items = dataState.playlists,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index, _ ->
                            selectedIndex = index
                            onPlaylistAction(PlaylistAction.SelectPlaylist(index))
                        })

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size4)
                ) {
                    items(
                        dataState.groups,
                        key = { it.groupName }
                    ) { item ->
                        PlaylistGroupItemView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onPlaylistAction(PlaylistAction.SelectGroup(item.groupName))

                                },
                            group = item
                        )
                    }
                }
            }

            if (dataState.isLoading) {
                LoadingView()
            }

            if (dataState.dataIsEmpty) {
                NoItemsView(modifier = Modifier
                    .fillMaxSize(),
                    navigateTitle = stringResource(id = R.string.pl_btn_add_first_playlist),
                    onNavigateClick = {
                        onPlaylistAction(PlaylistAction.NavigateToSettings)
                    })
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistDataView() {
    VideoAppTheme {
        PlaylistDataView(
            dataState = PlaylistDataViewModel.PlaylistDataState(
                groups = testChannelsGroups
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDataView() {
    VideoAppTheme(darkTheme = true) {
        PlaylistDataView(
            dataState = PlaylistDataViewModel.PlaylistDataState(
                groups = testChannelsGroups
            )
        )
    }
}