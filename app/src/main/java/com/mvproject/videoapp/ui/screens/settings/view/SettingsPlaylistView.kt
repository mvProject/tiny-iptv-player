/*
 *  Created by Medvediev Viktor [mvproject] on 10.05.23, 20:19
 *  Copyright © 2023
 *  last modified : 04.05.23, 16:42
 *
 */

package com.mvproject.videoapp.ui.screens.settings.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.navigation.PlaylistScreenRoute
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsPlaylistViewModel
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING

@Composable
fun SettingsPlaylistView(
    dataState: SettingsPlaylistViewModel.PlaylistSettingsState,
    onDeleteItem: (String) -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.pl_settings_playlist),
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

            PlaylistsView(
                modifier = Modifier
                    .weight(1f),
                list = dataState.playlists,
                onPlaylistDelete = onDeleteItem
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.size4)
                    .background(MaterialTheme.colors.primary)
            )

            Button(
                onClick = {
                    navigator.push(PlaylistScreenRoute(id = EMPTY_STRING))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(
                    MaterialTheme.dimens.size1,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(MaterialTheme.dimens.size8)
            ) {
                Text(text = stringResource(id = R.string.pl_btn_add_new))
            }
        }
    }
}


@Composable
fun PlaylistsView(
    modifier: Modifier = Modifier,
    list: List<Playlist>,
    onPlaylistDelete: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.background
            )
    ) {
        items(list, key = { it.id }) { item ->
            PlaylistsItemView(
                item = item,
                onPlaylistDelete = onPlaylistDelete
            )
        }
    }
}

@Composable
fun PlaylistsItemView(
    item: Playlist,
    onPlaylistDelete: (String) -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.size8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(MaterialTheme.dimens.weight1)
                .clickable {
                    navigator.push(PlaylistScreenRoute(id = item.id.toString()))
                }
        ) {
            Text(
                text = item.listName,
                fontSize = 16.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.size4),
                color = MaterialTheme.colors.onBackground
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.size8)
            )

            Text(
                text = item.listUrl,
                fontSize = 12.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = MaterialTheme.dimens.size4),
                color = MaterialTheme.colors.onBackground
            )
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.size8)
        )

        Icon(
            modifier = Modifier.clickable {
                onPlaylistDelete(item.id.toString())
            },
            imageVector = Icons.Outlined.Delete,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = null
        )
    }

}

@Composable
@Preview(showBackground = true)
private fun SettingsPlaylistViewPre() {
    SettingsPlaylistView(
        SettingsPlaylistViewModel.PlaylistSettingsState()
    )
}