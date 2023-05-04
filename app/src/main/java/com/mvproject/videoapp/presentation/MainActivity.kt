/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.videoapp.navigation.PlaylistContentScreenRoute
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = getViewModel()

            // todo this must be in workers

            //    viewModel.prepareInfo()
            //  viewModel.loadEpg()
            //  viewModel.loadAlterEpg()
            // viewModel.checkAllPlaylistsChannelsInfo()

            VideoAppTheme {
                // Surface(color = MaterialTheme.colors.background) {
                Navigator(
                    screen = PlaylistContentScreenRoute(),
                )
                //  }
            }
        }
    }
}