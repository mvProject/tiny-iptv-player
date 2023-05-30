/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.main.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.work.WorkInfo
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.tinyiptv.navigation.PlaylistDataRoute
import com.mvproject.tinyiptv.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import io.github.aakira.napier.Napier
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Napier.e("POST_NOTIFICATIONS permission denied or forever denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = koinViewModel()

            val infoUpdateState by viewModel.infoUpdateState.observeAsState()
            val alterUpdateState by viewModel.alterUpdateState.observeAsState()
            val mainUpdateState by viewModel.mainUpdateState.observeAsState()

            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
                        Napier.w("testing infoUpdateState state ${info.state}")
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
                        Napier.w("testing alterUpdateState state ${info.state}")
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
                        Napier.w("testing mainUpdateState state ${info.state}")
                        if (info.state != WorkInfo.State.RUNNING) {
                            viewModel.checkMainEpgUpdate()
                        }
                    }
                }
            }

            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightStatusBars = !isSystemInDarkTheme()

            VideoAppTheme {
                Navigator(
                    screen = PlaylistDataRoute()
                )
            }
        }
    }
}