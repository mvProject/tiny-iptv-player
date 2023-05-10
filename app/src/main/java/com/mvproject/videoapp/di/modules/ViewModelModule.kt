/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.ui.screens.PlaylistDataViewModel
import com.mvproject.videoapp.ui.screens.PlaylistGroupDataViewModel
import com.mvproject.videoapp.ui.screens.main.viewmodel.MainViewModel
import com.mvproject.videoapp.ui.screens.player.VideoViewViewModel
import com.mvproject.videoapp.ui.screens.playlist.AddPlayListViewModel
import com.mvproject.videoapp.ui.screens.settings.viewmodel.SettingsPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::VideoViewViewModel)
    viewModelOf(::AddPlayListViewModel)
    viewModelOf(::SettingsPlaylistViewModel)
    viewModelOf(::PlaylistGroupDataViewModel)
    viewModelOf(::PlaylistDataViewModel)
    viewModelOf(::MainViewModel)
}