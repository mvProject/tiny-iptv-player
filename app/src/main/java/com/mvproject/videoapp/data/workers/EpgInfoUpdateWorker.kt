/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 16:10
 *
 */

package com.mvproject.videoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mvproject.videoapp.data.manager.EpgManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EpgInfoUpdateWorker(
    appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val epgManager: EpgManager by inject()

    override suspend fun doWork(): Result {
        epgManager.prepareEpgInfo()

        return Result.success()
    }
}