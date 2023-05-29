/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 16:10
 *
 */

package com.mvproject.tinyiptv.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mvproject.tinyiptv.R
import com.mvproject.tinyiptv.data.manager.EpgManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlterEpgUpdateWorker(
    appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val epgManager: EpgManager by inject()

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())

        epgManager.getAlterEpg()

        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val id = UPDATE_ALTER_EPG_NOTIFICATION_CHANNEL_ID
        val notificationTitle = applicationContext.getString(R.string.wrk_epg_update_title)
        val notificationContent = applicationContext.getString(R.string.wrk_msg_alter_epg_update)

        val cancelTitle = applicationContext.getString(R.string.wrk_cancel)
        val cancelIntent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        createChannel()

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(notificationTitle)
            .setTicker(notificationTitle)
            .setContentText(notificationContent)
            .setSmallIcon(R.drawable.no_channel_logo)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancelTitle, cancelIntent)
            .build()

        return ForegroundInfo(UPDATE_ALTER_EPG_NOTIFICATION_ID, notification)
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            UPDATE_ALTER_EPG_NOTIFICATION_CHANNEL_ID,
            UPDATE_ALTER_EPG_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    private companion object {
        const val UPDATE_ALTER_EPG_NOTIFICATION_ID = 1001
        const val UPDATE_ALTER_EPG_NOTIFICATION_CHANNEL_NAME = "Update Alter Epg Notifications"
        const val UPDATE_ALTER_EPG_NOTIFICATION_CHANNEL_ID = "Update Alter Epg Notifications ID"
    }
}