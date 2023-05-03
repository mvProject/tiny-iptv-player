/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.models.playlist

import com.mvproject.videoapp.utils.AppConstants

data class Playlist(
    val id: Long,
    val listName: String,
    val listUrl: String,
    val lastUpdateDate: Long,
    val updatePeriod: Long,
    val isMainInfoUse: Boolean = true,
    val isAlterInfoUse: Boolean = true,
    val isRemoteSource: Boolean = false,

    ) {
    val isLocal
        get() = listUrl.contains(AppConstants.PLAYLIST_LOCAL_TYPE)

    val isRemote
        get() = listUrl.contains(AppConstants.PLAYLIST_REMOTE_TYPE) || listUrl.contains(AppConstants.PLAYLIST_REMOTE_SEC_TYPE)

    override fun toString(): String {
        return StringBuilder()
            .append("\n")
            .append("name - $listName")
            .append("\n")
            .append("listUrl - $listUrl")
            .append("\n")
            .append("isLocal - $isLocal")
            .append("\n")
            .append("updatePeriod - $updatePeriod")
            .append("\n")
            .append("lastUpdateDate - $lastUpdateDate")
            .append("\n")
            .append("isMainInfoUse - $isMainInfoUse, isAlterInfoUse - $isAlterInfoUse")
            .toString()
    }
}