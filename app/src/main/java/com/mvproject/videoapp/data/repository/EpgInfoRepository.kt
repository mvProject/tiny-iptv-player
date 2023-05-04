/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.repository

import com.mvproject.videoapp.VideoAppDatabase
import com.mvproject.videoapp.data.mappers.EntityMapper.toChannelInfoAlter
import com.mvproject.videoapp.data.mappers.ParseMappers.toChannelInfoAlterEntity
import com.mvproject.videoapp.data.mappers.ParseMappers.toChannelInfoMainEntity
import com.mvproject.videoapp.data.models.epg.ChannelInfoAlter
import com.mvproject.videoapp.data.models.parse.AvailableChannelParseModel
import com.mvproject.videoapp.data.models.parse.ChannelsInfoParseModel
import com.mvproject.videoapp.data.network.NetworkRepository
import com.mvproject.videoapp.data.parser.M3UParser
import io.github.aakira.napier.Napier
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class EpgInfoRepository(
    private val db: VideoAppDatabase,
    private val networkRepository: NetworkRepository,
    private val preferenceRepository: PreferenceRepository
) {

    private val queries = db.epgProgramQueries

    suspend fun prepareEpgInfo() {
        updateChannelInfoMainData()

        updateChannelInfoAlterData()
        Napier.i("testing prepareEpgInfo complete")
    }

    fun getEpgInfoByAlterIds(): List<ChannelInfoAlter> {
        return queries.getAlterEpgInfo().executeAsList().map {
            it.toChannelInfoAlter()
        }
    }

    private suspend fun updateChannelInfoAlterData() {
        val infoResult = networkRepository.loadAlterInfo().channels
        insertChannelInfoAlterData(infoData = infoResult)
        Napier.w("testing updateChannelInfoAlterData $infoResult")
        preferenceRepository.setAlterInfoExist(state = true)
        Napier.i("testing updateChannelInfoAlterData complete")
        // todo check available playlists and update (if not updated)
    }

    private suspend fun updateChannelInfoMainData() {
        val infoResult = parseChannelInfoMainData()
        insertChannelInfoMainData(infoData = infoResult)
        Napier.w("testing updateChannelInfoMainData $infoResult")
        preferenceRepository.setMainInfoExist(state = true)
        Napier.i("testing updateChannelInfoMainData complete")
        // todo check available playlists and update (if not updated)
    }

    private suspend fun parseChannelInfoMainData(): List<ChannelsInfoParseModel> {
        val resultStream = networkRepository.loadMainInfo().toInputStream()

        return buildList {
            BufferedReader(
                InputStreamReader(resultStream)
            ).use {
                it.readText().also { content ->
                    val result = M3UParser.parseInfo(content)
                    addAll(result)
                }
            }
        }
    }

    private suspend fun insertChannelInfoMainData(infoData: List<ChannelsInfoParseModel>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                infoData.forEach { item ->
                    if (item.id.isNotEmpty() && item.logo.isNotEmpty()) {
                        queries.insertChannelInfoMainEntity(
                            item.toChannelInfoMainEntity()
                        )
                    }
                }
            }
        }
    }

    private suspend fun insertChannelInfoAlterData(infoData: List<AvailableChannelParseModel>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                infoData.forEach { item ->
                    if (item.channelId.isNotEmpty() && item.channelIcon.isNotEmpty()) {
                        queries.insertChannelInfoAlterEntity(
                            item.toChannelInfoAlterEntity()
                        )
                    }
                }
            }
        }
    }
}