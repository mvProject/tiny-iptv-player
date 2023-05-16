/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:46
 *
 */

package com.mvproject.videoapp.di.modules

import com.mvproject.videoapp.data.repository.EpgInfoRepository
import com.mvproject.videoapp.data.repository.EpgProgramRepository
import com.mvproject.videoapp.data.repository.PlaylistChannelsRepository
import com.mvproject.videoapp.data.repository.PlaylistsRepository
import com.mvproject.videoapp.data.repository.PreferenceRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::PreferenceRepository)
    singleOf(::PlaylistsRepository)
    singleOf(::PlaylistChannelsRepository)
    singleOf(::EpgProgramRepository)
    singleOf(::EpgInfoRepository)
}