/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.presentation.PlaylistGroupDataMain
import com.mvproject.videoapp.presentation.PlaylistGroupDataViewModel
import org.koin.androidx.compose.getViewModel

data class PlaylistGroupContentScreenRoute(
    val group: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val playlistGroupDataViewModel: PlaylistGroupDataViewModel = getViewModel()

        PlaylistGroupDataMain(
            viewModel = playlistGroupDataViewModel,
            groupSelected = group
        )
    }
}