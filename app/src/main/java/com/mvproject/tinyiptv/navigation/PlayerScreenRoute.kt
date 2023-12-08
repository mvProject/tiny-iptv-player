/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 08.12.23, 13:08
 *
 */

package com.mvproject.tinyiptv.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.tinyiptv.ui.screens.player.VideoView
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import org.koin.androidx.compose.koinViewModel

data class PlayerScreenRoute(
    val mediaName: String,
    val mediaGroup: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val videoViewViewModel: VideoViewViewModel = koinViewModel()

        VideoView(
            viewModel = videoViewViewModel,
            channelName = mediaName,
            channelGroup = mediaGroup
        )
    }
}