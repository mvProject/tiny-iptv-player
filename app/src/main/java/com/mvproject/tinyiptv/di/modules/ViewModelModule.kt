/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:21
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.tinyiptv.ui.screens.player.VideoViewViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.viewmodel.AddPlayListViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.viewmodel.PlaylistDataViewModel
import com.mvproject.tinyiptv.ui.screens.playlist.viewmodel.PlaylistGroupViewModel
import com.mvproject.tinyiptv.ui.screens.settings.viewmodel.SettingsEpgViewModel
import com.mvproject.tinyiptv.ui.screens.settings.viewmodel.SettingsPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::VideoViewViewModel)
    viewModelOf(::AddPlayListViewModel)
    viewModelOf(::SettingsPlaylistViewModel)
    viewModelOf(::PlaylistGroupViewModel)
    viewModelOf(::PlaylistDataViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsEpgViewModel)
}