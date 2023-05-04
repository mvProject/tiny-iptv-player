/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.mappers

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mvproject.videoapp.data.models.channels.PlaylistChannel

object ListMappers {
    fun List<PlaylistChannel>.createMediaItems(): List<MediaItem> {
        return buildList {
            this@createMediaItems.forEach { video ->
                add(
                    MediaItem.Builder()
                        .setUri(video.channelUrl)
                        //   .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setDisplayTitle(video.channelName)
                                .build()
                        ).build()
                )
            }
        }
    }
}