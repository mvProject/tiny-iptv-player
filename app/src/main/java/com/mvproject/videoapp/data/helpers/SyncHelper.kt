/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.videoapp.data.helpers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mvproject.videoapp.data.repository.PreferenceRepository
import com.mvproject.videoapp.data.workers.AlterEpgUpdateWorker
import com.mvproject.videoapp.data.workers.EpgInfoUpdateWorker
import com.mvproject.videoapp.data.workers.MainEpgUpdateWorker
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_1
import com.mvproject.videoapp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import com.mvproject.videoapp.utils.typeToDuration
import io.github.aakira.napier.Napier

class SyncHelper(
    private val context: Context,
    private val preferenceRepository: PreferenceRepository
) {
    val infoUpdateState
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(UPDATE_EPG_INFO)
    val alterUpdateState
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(ALTER_EPG_WORKER)
    val mainUpdateState
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(MAIN_EPG_WORKER)

    suspend fun checkEpgInfoUpdate() {
        val lastEpgInfoUpdate = preferenceRepository.getEpgInfoLastUpdate()
        val updateElapsedTime = actualDate - lastEpgInfoUpdate
        val updateElapsedPeriod = typeToDuration(
            preferenceRepository.getEpgInfoUpdatePeriod()
        )
        if (updateElapsedPeriod in INT_VALUE_1 until updateElapsedTime) {
            launchEpgInfoUpdate()
        } else {
            Napier.e("testing checkEpgInfoUpdate not needed")
        }
    }

    suspend fun checkAlterEpgUpdate() {
        val lastEpgInfoUpdate = preferenceRepository.getAlterEpgLastUpdate() ?: LONG_VALUE_ZERO
        val updateElapsedTime = actualDate - lastEpgInfoUpdate
        val updateElapsedPeriod = typeToDuration(
            preferenceRepository.getAlterEpgUpdatePeriod()
        )
        if (updateElapsedPeriod in INT_VALUE_1 until updateElapsedTime) {
            launchAlterEpgUpdate()
        } else {
            Napier.e("testing checkAlterEpgUpdate not needed")
        }
    }

    suspend fun checkMainEpgUpdate() {
        val lastEpgInfoUpdate = preferenceRepository.getMainEpgLastUpdate()
        val updateElapsedTime = actualDate - lastEpgInfoUpdate
        val updateElapsedPeriod = typeToDuration(
            preferenceRepository.getMainEpgUpdatePeriod()
        )
        if (updateElapsedPeriod in INT_VALUE_1 until updateElapsedTime) {
            launchMainEpgUpdate()
        } else {
            Napier.e("testing checkMainEpgUpdate not needed")
        }
    }

    private fun launchEpgInfoUpdate() {
        Napier.w("testing launchEpgInfoUpdate")
        val channelRequest = OneTimeWorkRequest
            .Builder(EpgInfoUpdateWorker::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            UPDATE_EPG_INFO,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private fun launchAlterEpgUpdate() {
        Napier.w("testing launchAlterEpgUpdate")
        val channelRequest = OneTimeWorkRequest
            .Builder(AlterEpgUpdateWorker::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ALTER_EPG_WORKER,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private fun launchMainEpgUpdate() {
        Napier.w("testing launchAlterEpgUpdate")
        val channelRequest = OneTimeWorkRequest
            .Builder(MainEpgUpdateWorker::class.java)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            MAIN_EPG_WORKER,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private companion object {
        const val ALTER_EPG_WORKER = "UPDATE_ALTER_EPG"
        const val MAIN_EPG_WORKER = "UPDATE_MAIN_EPG"
        const val UPDATE_EPG_INFO = "UPDATE_EPG_INFO"

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}