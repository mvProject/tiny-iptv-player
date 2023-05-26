/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.tinyiptv.data.helpers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mvproject.tinyiptv.data.repository.PreferenceRepository
import com.mvproject.tinyiptv.data.workers.AlterEpgUpdateWorker
import com.mvproject.tinyiptv.data.workers.ChannelsUpdateWorker
import com.mvproject.tinyiptv.data.workers.EpgInfoUpdateWorker
import com.mvproject.tinyiptv.data.workers.MainEpgUpdateWorker
import com.mvproject.tinyiptv.utils.AppConstants.INT_VALUE_1
import com.mvproject.tinyiptv.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_ID
import com.mvproject.tinyiptv.utils.TimeUtils.actualDate
import com.mvproject.tinyiptv.utils.typeToDuration
import io.github.aakira.napier.Napier
import java.util.concurrent.TimeUnit

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

    private val workManager = WorkManager.getInstance(context)

    fun scheduleChannelsUpdate(playlistId: Long, updatePeriod: Long) {
        val updatePeriodDuration = typeToDuration(updatePeriod.toInt())

        val workName = CHANNELS_WORKER + playlistId

        val channelRequest = PeriodicWorkRequestBuilder<ChannelsUpdateWorker>(
            updatePeriodDuration, TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .setInitialDelay(updatePeriodDuration, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    PLAYLIST_ID to playlistId
                )
            )
            .build()


        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.UPDATE,
            channelRequest
        )
    }

    fun cancelScheduleChannelsUpdate(playlistId: Long) {
        val workName = CHANNELS_WORKER + playlistId
        workManager.cancelUniqueWork(workName)
    }

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
        val channelRequest = OneTimeWorkRequestBuilder<EpgInfoUpdateWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            UPDATE_EPG_INFO,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private fun launchAlterEpgUpdate() {
        Napier.w("testing launchAlterEpgUpdate")
        val channelRequest = OneTimeWorkRequestBuilder<AlterEpgUpdateWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            ALTER_EPG_WORKER,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private fun launchMainEpgUpdate() {
        Napier.w("testing launchAlterEpgUpdate")
        val channelRequest = OneTimeWorkRequestBuilder<MainEpgUpdateWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            MAIN_EPG_WORKER,
            ExistingWorkPolicy.KEEP,
            channelRequest
        )
    }

    private companion object {
        const val CHANNELS_WORKER = "CHANNELS_WORKER"
        const val ALTER_EPG_WORKER = "UPDATE_ALTER_EPG"
        const val MAIN_EPG_WORKER = "UPDATE_MAIN_EPG"
        const val UPDATE_EPG_INFO = "UPDATE_EPG_INFO"

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}