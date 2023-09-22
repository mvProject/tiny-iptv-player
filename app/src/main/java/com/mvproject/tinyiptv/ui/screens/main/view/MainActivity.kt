/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.ui.screens.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import cafe.adriel.voyager.navigator.Navigator
import com.mvproject.tinyiptv.navigation.PlaylistDataRoute
import com.mvproject.tinyiptv.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tinyiptv.ui.theme.VideoAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
/*    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Napier.e("POST_NOTIFICATIONS permission denied or forever denied")
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = koinViewModel()

            /*            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
                        }*/

            viewModel.checkUpdates()

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