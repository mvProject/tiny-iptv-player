/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:39
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.helpers.SyncHelper
import com.mvproject.tinyiptv.data.workers.AlterEpgUpdateWorker
import com.mvproject.tinyiptv.data.workers.EpgInfoUpdateWorker
import com.mvproject.tinyiptv.data.workers.MainEpgUpdateWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val workerModule = module {
    singleOf(::SyncHelper)
    workerOf(::EpgInfoUpdateWorker)
    workerOf(::AlterEpgUpdateWorker)
    workerOf(::MainEpgUpdateWorker)
}