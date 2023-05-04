/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.components.channels.ChannelCardView
import com.mvproject.videoapp.components.channels.ChannelGridView
import com.mvproject.videoapp.components.channels.ChannelListView
import com.mvproject.videoapp.components.toolbars.AppBarWithSearch
import com.mvproject.videoapp.data.enums.ChannelsViewType
import com.mvproject.videoapp.data.models.channels.PlaylistChannelWithEpg
import com.mvproject.videoapp.navigation.PlayerScreenRoute
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_1
import io.github.aakira.napier.Napier

@Composable
fun PlaylistGroupDataMain(
    viewModel: PlaylistGroupDataViewModel,
    groupSelected: String
) {

    val navigator = LocalNavigator.currentOrThrow

    SideEffect {
        Napier.e("testing PlaylistGroupDataMain SideEffect")
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.loadChannelsByGroups(groupSelected)
    }

    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithSearch(
                appBarTitle = viewState.currentGroup,
                searchTextState = viewState.searchString,
                searchWidgetState = viewState.isSearching,
                onBackClick = { navigator.pop() },
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                        .background(
                            color = MaterialTheme.colors.background
                        ),
                    columns = columns,
                    state = rememberLazyGridState(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.size2),
                    contentPadding = PaddingValues(MaterialTheme.dimens.size2),
                    content = {
                        items(channelsList, key = { it.id }) { item ->
                            ChannelView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        navigator.push(
                                            PlayerScreenRoute(
                                                mediaId = item.id.toString(),
                                                mediaGroup = groupSelected
                                            )
                                        )
                                    }
                                    .background(
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(MaterialTheme.dimens.size12)
                                    ),
                                viewType = viewState.viewType,
                                item = item,
                                onEpgRequest = viewModel::updateChannelInfo,
                                onFavoriteClick = viewModel::toggleFavourites
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChannelView(
    modifier: Modifier = Modifier,
    viewType: ChannelsViewType,
    item: PlaylistChannelWithEpg,
    onEpgRequest: (PlaylistChannelWithEpg) -> Unit = {},
    onFavoriteClick: (PlaylistChannelWithEpg) -> Unit = {}
) {
    when (viewType) {
        ChannelsViewType.LIST -> {
            ChannelListView(
                modifier = modifier,
                channel = item,
                onEpgRequest = onEpgRequest,
                onFavoriteClick = onFavoriteClick
            )
        }

        ChannelsViewType.GRID -> {
            ChannelGridView(
                modifier = modifier,
                channel = item,
                onEpgRequest = onEpgRequest
            )
        }

        ChannelsViewType.CARD -> {
            ChannelCardView(
                modifier = modifier,
                channel = item
            )
        }
    }
}