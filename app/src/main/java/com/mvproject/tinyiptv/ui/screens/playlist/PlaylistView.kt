/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 15:23
 *
 */

package com.mvproject.tinyiptv.ui.screens.playlist

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.enums.UpdatePeriod
import com.mvproject.tinyiptv.ui.components.OptionSelector
import com.mvproject.tinyiptv.ui.components.overlay.OverlayContent
import com.mvproject.tinyiptv.ui.components.overlay.OverlayOptionsMenu
import com.mvproject.tinyiptv.ui.components.toolbars.AppBarWithBackNav
import com.mvproject.tinyiptv.ui.components.views.LoadingView
import com.mvproject.tinyiptv.ui.screens.playlist.action.PlaylistAction
import com.mvproject.tinyiptv.ui.screens.playlist.state.PlaylistState
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import com.mvproject.tinyiptv.ui.theme.dimens
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_MIME_TYPE
import com.mvproject.tinyiptv.utils.AppConstants.WEIGHT_1

@Composable
fun PlaylistView(
    state: PlaylistState,
    onNavigateBack: () -> Unit = {},
    onPlaylistAction: (PlaylistAction) -> Unit = {}
) {
    val fileSelectLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            onPlaylistAction(PlaylistAction.SetLocalUri(uri.toString()))
        }
    )
    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = stringResource(id = R.string.msg_playlist_details),
                onBackClick = onNavigateBack,
            )
        },
        bottomBar = {
            ElevatedButton(
                enabled = state.isReadyToSave,
                onClick = {
                    if (state.isEdit) {
                        onPlaylistAction(PlaylistAction.UpdatePlaylist)
                    } else {
                        onPlaylistAction(PlaylistAction.SavePlaylist)
                    }
                },
                modifier = Modifier
                    .padding(MaterialTheme.dimens.size8)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.small
            ) {
                val text = if (state.isEdit) {
                    stringResource(R.string.btn_update)
                } else {
                    stringResource(R.string.btn_save)
                }

                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
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
            val isUpdateOptionOpen = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.size12),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.listName,
                    onValueChange = {
                        onPlaylistAction(PlaylistAction.SetTitle(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.hint_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))


                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLocal,
                    value = if (state.isLocal) state.localName else state.url,
                    onValueChange = {
                        onPlaylistAction(PlaylistAction.SetRemoteUrl(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.hint_address),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.DarkGray,
                        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size8))

                OptionSelector(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.hint_update_period),
                    enabled = !state.isLocal,
                    selectedItem = stringResource(
                        id = UpdatePeriod.entries[state.updatePeriod].title
                    ),
                    isExpanded = isUpdateOptionOpen.value,
                    onClick = {
                        isUpdateOptionOpen.value = true
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = MaterialTheme.dimens.size8
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = stringResource(id = R.string.label_or),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Divider(
                        modifier = Modifier.weight(WEIGHT_1),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size16))

                OutlinedButton(
                    onClick = { fileSelectLauncher.launch(arrayOf(PLAYLIST_MIME_TYPE)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_add_local),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (state.isSaving) {
                LoadingView()
            }

            OverlayContent(
                isVisible = isUpdateOptionOpen.value,
                contentAlpha = MaterialTheme.dimens.alpha90,
                onViewTap = { isUpdateOptionOpen.value = false }
            ) {
                OverlayOptionsMenu(
                    title = stringResource(id = R.string.hint_update_period),
                    selectedIndex = state.updatePeriod,
                    items = UpdatePeriod.entries.map {
                        stringResource(id = it.title)
                    },
                    onItemSelected = { index ->
                        onPlaylistAction(PlaylistAction.SetUpdatePeriod(index))
                        isUpdateOptionOpen.value = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlaylistDetailViewContent() {
    VideoAppTheme {
        PlaylistView(
            state = PlaylistState()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDarkPlaylistDetailViewContent() {
    VideoAppTheme(darkTheme = true) {
        PlaylistView(
            state = PlaylistState()
        )
    }
}
