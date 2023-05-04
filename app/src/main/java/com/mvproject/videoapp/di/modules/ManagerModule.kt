/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.data.manager.EpgManager
import com.mvproject.videoapp.data.manager.PlaylistChannelManager
import com.mvproject.videoapp.data.manager.PlaylistManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val managerModule = module {
    singleOf(::EpgManager)
    singleOf(::PlaylistChannelManager)
    singleOf(::PlaylistManager)
}