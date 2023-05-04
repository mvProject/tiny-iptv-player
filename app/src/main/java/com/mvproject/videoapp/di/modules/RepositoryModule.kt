/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.di

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
    //single {
    //    ChannelPagingDataSource(get(), get(), get(), get())
    //}
}