/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.presentation.MainViewModel
import com.mvproject.videoapp.presentation.PlaylistDataViewModel
import com.mvproject.videoapp.presentation.PlaylistGroupDataViewModel
import com.mvproject.videoapp.presentation.player.VideoViewViewModel
import com.mvproject.videoapp.presentation.playlist.AddPlayListViewModel
import com.mvproject.videoapp.presentation.settings.SettingsPlaylistViewModel
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