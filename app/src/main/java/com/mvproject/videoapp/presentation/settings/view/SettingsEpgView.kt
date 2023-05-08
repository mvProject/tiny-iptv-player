/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.presentation.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mvproject.videoapp.components.toolbars.AppBarWithBackNav

@Composable
fun SettingsEpgView() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            AppBarWithBackNav(
                appBarTitle = "Settings Epg",
                onBackClick = { navigator.pop() },
            )
        }
    ) {

    }
}