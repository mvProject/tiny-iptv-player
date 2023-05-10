/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 16:41
 *  Copyright © 2023
 *  last modified : 04.05.23, 11:58
 *
 */

package com.mvproject.videoapp.presentation.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.work.WorkInfo
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.videoapp.navigation.PlaylistContentScreenRoute
import com.mvproject.videoapp.presentation.main.viewmodel.MainViewModel
import com.mvproject.videoapp.ui.theme.VideoAppTheme
import com.mvproject.videoapp.utils.setOrientation
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Napier.w("testing permission POST_NOTIFICATIONS granted")
        } else {
            Napier.e("testing permission POST_NOTIFICATIONS permission denied or forever denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = getViewModel()

            val infoUpdateState by viewModel.infoUpdateState.observeAsState()
            val alterUpdateState by viewModel.alterUpdateState.observeAsState()
            val mainUpdateState by viewModel.mainUpdateState.observeAsState()

            if (
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                Napier.w("testing permission POST_NOTIFICATIONS granted")
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // show rationale and then launch launcher to request permission
                    Napier.w("testing permission POST_NOTIFICATIONS shouldShowRequestPermissionRationale")
                } else {
                    // first request or forever denied case
                    Napier.e("testing permission POST_NOTIFICATIONS first request or forever denied case")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            infoUpdateState?.let { states ->
                if (states.isEmpty()) {
                    viewModel.checkEpgInfoUpdate()
                } else {
                    states.firstOrNull()?.let { info ->
                        if (info.state != WorkInfo.State.RUNNING) {
                            viewModel.checkEpgInfoUpdate()
                        }
                    }
                }
            }

            alterUpdateState?.let { states ->
                if (states.isEmpty()) {
                    viewModel.checkAlterEpgUpdate()
                } else {
                    states.firstOrNull()?.let { info ->
                        if (info.state != WorkInfo.State.RUNNING) {
                            viewModel.checkAlterEpgUpdate()
                        }
                    }
                }
            }

            mainUpdateState?.let { states ->
                if (states.isEmpty()) {
                    viewModel.checkMainEpgUpdate()
                } else {
                    states.firstOrNull()?.let { info ->
                        if (info.state != WorkInfo.State.RUNNING) {
                            viewModel.checkMainEpgUpdate()
                        }
                    }
                }
            }

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