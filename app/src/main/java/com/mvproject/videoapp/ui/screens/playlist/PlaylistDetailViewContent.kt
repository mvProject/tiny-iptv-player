/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:26
 *
 */

package com.mvproject.videoapp.ui.screens.playlist

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.UpdatePeriod
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.components.views.LoadingView
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.ALPHA_50
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_MIME_TYPE
import io.github.aakira.napier.Napier

@Composable
fun PlaylistDetailViewContent(
    playlistId: String = EMPTY_STRING,
    state: PlayListState,
    onPlaylistAction: (PlayListAction) -> Unit = {}
) {
    val navigator = LocalNavigator.currentOrThrow

    val fileSelectLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            onPlaylistAction(PlayListAction.ChangeUrl(uri.toString()))
        }
    )

    LaunchedEffect(key1 = playlistId) {
        Napier.w("testing PlayListViewContent playlistId:$playlistId")
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.pl_msg_playlist_details),
                onBackClick = { navigator.pop() },
            )
        },

        bottomBar = {
            Button(
                enabled = state.isReadyToSave,
                onClick = {
                    if (state.isComplete) {
                        navigator.pop()
                    } else {
                        onPlaylistAction(PlayListAction.SavePlaylist)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size12),
                border = BorderStroke(
                    MaterialTheme.dimens.size1,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
                shape = RoundedCornerShape(MaterialTheme.dimens.size12)
            ) {
                Text(
                    text = if (state.isComplete) {
                        stringResource(R.string.pl_btn_close)
                    } else {
                        stringResource(R.string.pl_btn_save)
                    }
                )
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
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.size12),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = state.listName,
                    onValueChange = {
                        onPlaylistAction(PlayListAction.ChangeName(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.pl_hint_name))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onSurface,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface
                            .copy(alpha = ALPHA_50),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                TextField(
                    value = state.listUri,
                    enabled = state.isRemote,
                    onValueChange = {
                        onPlaylistAction(PlayListAction.ChangeUrl(it))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.pl_hint_address))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.onPrimary,
                        focusedIndicatorColor = MaterialTheme.colors.onSurface,
                        unfocusedIndicatorColor = MaterialTheme.colors.onSurface
                            .copy(alpha = ALPHA_50),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                Button(
                    onClick = {
                        fileSelectLauncher.launch(arrayOf(PLAYLIST_MIME_TYPE))
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
                    shape = RoundedCornerShape(MaterialTheme.dimens.size12)
                ) {
                    Text(text = stringResource(id = R.string.pl_btn_add_local))
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                LargeDropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.isRemote,
                    label = stringResource(id = R.string.pl_hint_update_period),
                    items = UpdatePeriod.values().map {
                        stringResource(id = it.title)
                    },
                    selectedIndex = state.updatePeriod,
                    onItemSelected = { index, _ ->
                        onPlaylistAction(PlayListAction.ChangeUpdatePeriod(index))
                    }
                )
            }

            if (state.isSaving) {
                LoadingView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistDetailViewContentPreview() {
    VideoAppTheme {
        PlaylistDetailViewContent(
            state = PlayListState()
        )
    }
}
