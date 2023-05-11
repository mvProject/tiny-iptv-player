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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mvproject.videoapp.R
import com.mvproject.videoapp.data.enums.UpdatePeriod
import com.mvproject.videoapp.ui.components.menu.LargeDropdownMenu
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.ui.theme.dimens
import com.mvproject.videoapp.utils.AppConstants.ALPHA_50
import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.videoapp.utils.AppConstants.PLAYLIST_MIME_TYPE
import io.github.aakira.napier.Napier

@Composable
fun PlayListView(
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

    LaunchedEffect(key1 = Unit) {
        Napier.w("testing PlayListView playlistId:$playlistId")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
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

                Spacer(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.size8)
                )

                TextField(
                    value = state.listDisplayUri,
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

                Spacer(
                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.size8)
                )

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

                Spacer(
                    modifier = Modifier.height(MaterialTheme.dimens.size8)
                )

                var selectedIndex by remember { mutableStateOf(INT_VALUE_ZERO) }
                LargeDropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !state.isLocal,
                    label = stringResource(id = R.string.pl_hint_update_period),
                    items = UpdatePeriod.values().map {
                        stringResource(id = it.title)
                    },
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, _ ->
                        selectedIndex = index
                        onPlaylistAction(PlayListAction.ChangeUpdatePeriod(index))
                    }
                )
            }

            if (state.isSaving) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onPrimary
                )
                // LottieProgress()
            }

            Spacer(
                modifier = Modifier.padding(vertical = MaterialTheme.dimens.size8)
            )

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
    }
}

@Composable
fun LottieProgress() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets5.lottiefiles.com/packages/lf20_Upp2W1xDta.json"))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}

@Composable
fun LottieComplete() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets3.lottiefiles.com/packages/lf20_OMID2wcuSC.json"))
    LottieAnimation(composition = composition)
}

@Preview(showBackground = true)
@Composable
fun AddPlaylistViewPreview() {
    VideoAppTheme {
        PlayListView(
            state = PlayListState()
        )
    }
}