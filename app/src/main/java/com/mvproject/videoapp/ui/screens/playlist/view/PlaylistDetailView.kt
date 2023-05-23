/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 23.05.23, 13:16
 *
 */

package com.mvproject.videoapp.ui.screens.playlist.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.UpdatePeriod
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.videoapp.ui.components.views.LoadingView
import com.mvproject.videoapp.ui.screens.playlist.actions.PlaylistDetailAction
import com.mvproject.videoapp.ui.screens.playlist.viewmodel.PlayListState
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_MIME_TYPE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailView(
    state: PlayListState,
    onPlaylistAction: (PlaylistDetailAction) -> Unit = {}
) {
    val fileSelectLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            onPlaylistAction(PlaylistDetailAction.ChangeUrl(uri.toString()))
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.pl_msg_playlist_details),
                onBackClick = { onPlaylistAction(PlaylistDetailAction.NavigateBack) },
            )
        },
        bottomBar = {
            ElevatedButton(
                enabled = state.isReadyToSave,
                onClick = {
                    if (state.isComplete) {
                        onPlaylistAction(PlaylistDetailAction.NavigateBack)
                    } else {
                        onPlaylistAction(PlaylistDetailAction.SavePlaylist)
                    }
                },
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                val text = if (state.isComplete) {
                    stringResource(R.string.pl_btn_close)
                } else {
                    stringResource(R.string.pl_btn_save)
                }

                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size12),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.listName,
                    onValueChange = {
                        onPlaylistAction(PlaylistDetailAction.ChangeName(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.pl_hint_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.listUri,
                    onValueChange = {
                        onPlaylistAction(PlaylistDetailAction.ChangeUrl(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.pl_hint_address),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                OutlinedButton(
                    onClick = { fileSelectLauncher.launch(arrayOf(PLAYLIST_MIME_TYPE)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(id = R.string.pl_btn_add_local),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.bodyLarge
                    )
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
                        onPlaylistAction(PlaylistDetailAction.ChangeUpdatePeriod(index))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.pl_chb_use_main_epg),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = state.isUsingMainEpg,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.tertiaryContainer,
                            checkedTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        onCheckedChange = { state ->
                            onPlaylistAction(
                                PlaylistDetailAction.ChangeMainEpgUsingState(
                                    state
                                )
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.pl_chb_use_alter_epg),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = state.isUsingAlterEpg,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.tertiaryContainer,
                            checkedTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary
                        ),
                        onCheckedChange = { state ->
                            onPlaylistAction(
                                PlaylistDetailAction.ChangeAlterEpgUsingState(
                                    state
                                )
                            )
                        }
                    )
                }
            }

            if (state.isSaving) {
                LoadingView()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistDetailViewContent() {
    VideoAppTheme {
        PlaylistDetailView(
            state = PlayListState()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDetailViewContent() {
    VideoAppTheme(darkTheme = true) {
        PlaylistDetailView(
            state = PlayListState()
        )
    }
}
