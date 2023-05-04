/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import com.mvproject.videoapp.presentation.settings.SettingsView

class SettingsScreenRoute : AndroidScreen() {

    @Composable
    override fun Content() {
        SettingsView()
    }
}