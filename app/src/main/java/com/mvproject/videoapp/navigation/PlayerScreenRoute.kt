/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.presentation.player.VideoView
import com.mvproject.videoapp.presentation.player.VideoViewViewModel
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