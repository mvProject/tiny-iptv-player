/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 10:36
 *
 */

package com.mvproject.videoapp.ui.screens.playlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.videoapp.data.enums.ChannelsViewType
import com.mvproject.videoapp.ui.components.channels.ChannelView
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithSearch
import com.mvproject.videoapp.ui.components.views.LoadingView
import com.mvproject.videoapp.ui.screens.playlist.actions.PlaylistGroupAction
import com.mvproject.videoapp.ui.screens.playlist.viewmodel.PlaylistGroupViewModel
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistGroupView(
    viewModel: PlaylistGroupViewModel,
    onGroupAction: (PlaylistGroupAction) -> Unit = {},
    groupSelected: String
) {
    LaunchedEffect(key1 = groupSelected) {
        viewModel.loadChannelsByGroups(groupSelected)
    }

    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            AppBarWithSearch(
                appBarTitle = viewState.currentGroup,
                searchTextState = viewState.searchString,
                searchWidgetState = viewState.isSearching,
                onBackClick = { onGroupAction(PlaylistGroupAction.NavigateBack) },
                onSearchTriggered = viewModel::onSearchTriggered,
                onViewTypeChange = viewModel::onViewTypeChange,
                onTextChange = viewModel::onSearchTextChange
            )
        }
    ) { paddingValues ->
        val channelsList by remember {
            derivedStateOf {
                if (viewState.searchString.length > 1)
                    viewModel.channels.filter {
                        it.channelName.contains(viewState.searchString, true)
                    }
                else viewModel.channels
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val columns = when (viewState.viewType) {
                ChannelsViewType.LIST -> {
                    GridCells.Fixed(INT_VALUE_1)
                }

                else -> {
                    GridCells.Adaptive(190.dp)
                }
            }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                columns = columns,
                state = rememberLazyGridState(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2),
                contentPadding = PaddingValues(MaterialTheme.dimens.size2),
                content = {
                    items(
                        channelsList,
                        key = { it.id }
                    ) { item ->
                        ChannelView(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    onGroupAction(
                                        PlaylistGroupAction.NavigateToGroup(item.id.toString())
                                    )
                                },
                            viewType = viewState.viewType,
                            item = item,
                            onEpgRequest = viewModel::updateChannelInfo,
                            onFavoriteClick = viewModel::toggleFavourites
                        )
                    }
                }
            )
            if (viewState.isLoading) {
                LoadingView()
            }
        }
    }
}