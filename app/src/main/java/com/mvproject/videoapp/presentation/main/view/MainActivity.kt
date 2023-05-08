/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 16:41
 *  Copyright © 2023
 *  last modified : 04.05.23, 11:58
 *
 */

package com.mvproject.videoapp.presentation.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.videoapp.navigation.PlaylistContentScreenRoute
import com.mvproject.videoapp.presentation.main.viewmodel.MainViewModel
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.utils.setOrientation
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = getViewModel()

            // todo this must be in workers

            // viewModel.prepareInfo()
            //  viewModel.loadEpg()
            //  viewModel.loadAlterEpg()

            val windowSizeClass = calculateWindowSizeClass(this)
            setOrientation(windowSizeClass)

            VideoAppTheme {
                Navigator(
                    screen = PlaylistContentScreenRoute(),
                )
            }
        }
    }
}