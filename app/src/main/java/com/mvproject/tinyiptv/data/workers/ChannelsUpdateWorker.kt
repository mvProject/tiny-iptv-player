/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 16:10
 *
 */

package com.mvproject.tinyiptv.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mvproject.tinyiptv.data.manager.PlaylistManager
import com.mvproject.tinyiptv.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.tinyiptv.utils.AppConstants.PLAYLIST_ID
import io.github.aakira.napier.Napier
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChannelsUpdateWorker(
    appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val playlistManager: PlaylistManager by inject()

    override suspend fun doWork(): Result {
        val playlistId =
            inputData.getLong(PLAYLIST_ID, LONG_NO_VALUE)
        Napier.w("testing playlistId $playlistId")

        val playlist = playlistManager.loadPlaylistById(playlistId)
        playlist?.let {
            Napier.w("testing update selected remote playlist $it")
            playlistManager.savePlaylistData(it)
        }

        return Result.success()
    }
}