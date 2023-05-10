/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:27
 *
 */

package com.mvproject.videoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.models.channels.ChannelsGroup
import com.mvproject.videoapp.navigation.PlaylistGroupContentScreenRoute
import com.mvproject.videoapp.navigation.SettingsScreenRoute
import com.mvproject.videoapp.ui.components.errors.NoItemsView
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithSettings
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_ZERO
import io.github.aakira.napier.Napier

@Composable
fun PlaylistGroupContent(
    dataState: PlaylistDataViewModel.PlaylistDataState,
    playlistState: PlaylistDataViewModel.PlaylistState,
    onPlaylistChange: (Int) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithSettings() {
                navigator.push(SettingsScreenRoute())
            }
        }) { paddingValues ->
        Napier.w("testing PlaylistGroupContent isLoading: ${dataState.isLoading}")

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.dimens.size8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (dataState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onPrimary
                )
            } else {
                if (playlistState.isPlaylistSelectorVisible) {
                    var selectedIndex by remember {
                        mutableStateOf(playlistState.playlistSelectedIndex)
                    }
                    LargeDropdownMenu(modifier = Modifier.fillMaxWidth(),
                        label = stringResource(id = R.string.pl_hint_current_playlist),
                        items = playlistState.playlists,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index, _ ->
                            selectedIndex = index
                            onPlaylistChange(index)
                        })

                    Spacer(
                        modifier = Modifier.height(MaterialTheme.dimens.size8)
                    )
                }

                if (dataState.groups.isEmpty()) {
                    NoItemsView(
                        navigateTitle = "Press to add"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(color = MaterialTheme.colors.background),
                    ) {
                        items(dataState.groups, key = { it.groupName }) { item ->
                            PlaylistGroupItemView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navigator.push(PlaylistGroupContentScreenRoute(group = item.groupName))
                                    }, group = item
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistGroupItemView(
    modifier: Modifier = Modifier, group: ChannelsGroup
) {

    Row(
        modifier = modifier.wrapContentSize(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = null,
            modifier = Modifier
                .size(MaterialTheme.dimens.size42)
                .clip(RoundedCornerShape(MaterialTheme.dimens.size4))
        )

        Spacer(
            modifier = Modifier.width(MaterialTheme.dimens.size4)
        )

        Text(
            text = group.groupName,
            fontSize = MaterialTheme.dimens.font14,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .weight(MaterialTheme.dimens.weight5)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colors.onBackground
        )

        if (group.groupContentCount > INT_VALUE_ZERO) {
            Spacer(
                modifier = Modifier.width(MaterialTheme.dimens.size4)
            )

            Text(
                text = group.groupContentCount.toString(),
                fontSize = MaterialTheme.dimens.font18,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .weight(MaterialTheme.dimens.weight1)
                    .align(Alignment.CenterVertically),
                color = MaterialTheme.colors.onBackground
            )
        }

    }
}