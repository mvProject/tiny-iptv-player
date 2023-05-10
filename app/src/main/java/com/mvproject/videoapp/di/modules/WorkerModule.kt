/*
 *  Created by Medvediev Viktor [mvproject] on 09.05.23, 12:29
 *  Copyright © 2023
 *  last modified : 09.05.23, 12:29
 *
 */

package com.mvproject.videoapp.di.modules

import com.mvproject.videoapp.data.helpers.SyncHelper
import com.mvproject.videoapp.data.workers.EpgInfoUpdateWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val workerModule = module {
    singleOf(::SyncHelper)
    workerOf(::EpgInfoUpdateWorker)
}