/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.09.23, 12:24
 *
 */

package com.mvproject.tinyiptv.ui.screens.channels

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mvproject.tinyiptv.data.enums.ChannelsViewType
import com.mvproject.tinyiptv.data.models.channels.TvPlaylistChannel
import com.mvproject.tinyiptv.ui.components.ConnectionState
import com.mvproject.tinyiptv.ui.components.channels.ChannelView
import com.mvproject.tinyiptv.ui.components.dialogs.ChannelOptionsDialog
import com.mvproject.tinyiptv.ui.components.networkConnectionState
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithSearch
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.screens.channels.action.TvPlaylistChannelAction
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
import io.github.aakira.napier.Napier

@Composable
fun TvPlaylistChannelsView(
    viewModel: TvPlaylistChannelsViewModel,
    onNavigateSelected: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onAction: (TvPlaylistChannelAction) -> Unit,
    groupSelected: String
) {
    LaunchedEffect(key1 = groupSelected) {
        viewModel.loadChannelsByGroups(groupSelected)
    }

    val connection by networkConnectionState()

    when (connection) {
        ConnectionState.Available -> Napier.w("testing connectivity is available")
        ConnectionState.Unavailable -> Napier.w("testing connectivity is unavailable")
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
                onBackClick = onNavigateBack,
                onSearchTriggered = {
                    onAction(TvPlaylistChannelAction.SearchTriggered)
                },
                onViewTypeChange = { type ->
                    onAction(TvPlaylistChannelAction.ViewTypeChange(type))
                },
                onTextChange = { text ->
                    onAction(TvPlaylistChannelAction.SearchTextChange(text))
                }
            )
        }
    ) { paddingValues ->

        val isChannelOptionOpen = remember { mutableStateOf(false) }
        var selected by remember {
            mutableStateOf<TvPlaylistChannel?>(null)
        }

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
                        key = { it.channelUrl }
                    ) { item ->
                        ChannelView(
                            modifier = Modifier.fillMaxSize(),
                            viewType = viewState.viewType,
                            item = item,
                            onOptionSelect = {
                                selected = item
                                isChannelOptionOpen.value = true
                            },
                            onChannelSelect = {
                                Napier.e("testing channel selected $item")
                                onNavigateSelected(item.channelUrl)
                            }
                        )
                    }
                }
            )
            if (viewState.isLoading) {
                LoadingView()
            }

            ChannelOptionsDialog(
                isDialogOpen = isChannelOptionOpen,
                isInFavorite = selected?.isInFavorites ?: false,
                isEpgEnabled = selected?.isEpgUsing ?: false,
                isEpgUsing = !selected?.epgId.isNullOrEmpty(),
                onToggleFavorite = {
                    selected?.let {
                        onAction(TvPlaylistChannelAction.ToggleFavourites(it))
                        selected = null
                    }
                    isChannelOptionOpen.value = false
                },
                onToggleEpgState = {
                    selected?.let {
                        onAction(TvPlaylistChannelAction.ToggleEpgState(it))
                        selected = null
                    }
                    isChannelOptionOpen.value = false

                },
                onShowEpg = {
                    onAction(TvPlaylistChannelAction.ToggleEpgVisibility)
                    isChannelOptionOpen.value = false
                }
            )
        }
    }
}