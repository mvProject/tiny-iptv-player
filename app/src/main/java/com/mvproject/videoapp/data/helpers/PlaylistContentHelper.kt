/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.mvproject.videoapp.data.models.channels.PlaylistChannel
import com.mvproject.videoapp.data.models.parse.PlaylistChannelParseModel
import com.mvproject.videoapp.data.models.playlist.Playlist
import com.mvproject.videoapp.data.network.NetworkRepository
import com.mvproject.videoapp.data.parser.M3UParser
import io.github.aakira.napier.Napier
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class PlaylistContentHelper(
    private val context: Context,
    private val networkRepository: NetworkRepository
) {

    suspend fun getPlaylistData(playlist: Playlist): List<PlaylistChannel> {
        Napier.w("testing savePlaylist is local:${playlist.isLocal}")

        val playlistData = if (playlist.isLocal) {
            getFromLocalPlaylist(Uri.parse(playlist.listUrl))
        } else {
            getFromRemotePlaylist(playlist.listUrl)
        }

        return parsePlayListData(playlist.id, playlistData)
    }

    private fun parsePlayListData(
        listId: Long,
        data: List<PlaylistChannelParseModel>
    ): List<PlaylistChannel> {
        return buildList {
            data.forEach { m3u ->
                add(
                    PlaylistChannel(
                        channelName = m3u.mChannel,
                        channelLogo = m3u.mLogoURL,
                        channelUrl = m3u.mStreamURL,
                        channelGroup = m3u.mGroupTitle,
                        epgId = null,
                        epgAlterId = null,
                        parentListId = listId
                    )
                )
            }
        }
    }

    @SuppressLint("Recycle")
    private fun getFromLocalPlaylist(uri: Uri): List<PlaylistChannelParseModel> {
        val file = context.contentResolver
            .openFileDescriptor(uri, MODE_READ_ONLY)
            ?.fileDescriptor

        return buildList {
            BufferedReader(
                InputStreamReader(
                    FileInputStream(file)
                )
            ).use { reader ->
                reader.readText().also { content ->
                    val result = M3UParser.parsePlaylist(content)
                    addAll(result)
                }
            }
        }
    }

    private suspend fun getFromRemotePlaylist(url: String): List<PlaylistChannelParseModel> {
        val resultStream = networkRepository.loadPlaylistData(url)
            .bodyAsChannel()
            .toInputStream()

        return buildList {
            BufferedReader(
                InputStreamReader(resultStream)
            ).use { reader ->
                reader.readText().also { content ->
                    val result = M3UParser.parsePlaylist(content)
                    addAll(result)
                }
            }
        }
    }

    private companion object {
        const val MODE_READ_ONLY = "r"
    }

}