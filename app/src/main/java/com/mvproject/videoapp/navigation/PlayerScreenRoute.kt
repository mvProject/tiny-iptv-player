/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.ui.screens.player.VideoView
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.koinViewModel

data class PlayerScreenRoute(val mediaId: String, val mediaGroup: String) : AndroidScreen() {

    @Composable
    override fun Content() {
        val videoViewViewModel: VideoViewViewModel = koinViewModel()
        Napier.i("testing1 PlayerScreenRoute Content")
        VideoView(
            viewModel = videoViewViewModel,
            channelId = mediaId,
            channelGroup = mediaGroup
        )
    }
}