/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 20:59
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

        val filtered =
            infoResult.filter { it.channelId.isNotEmpty() && it.channelIcon.isNotEmpty() }

        val properList = buildList {
            filtered.forEach { chn ->
                if (chn.channelNames.contains(CHANNEL_NAME_SPLIT_DELIMITER)) {
                    val splitNames = chn.channelNames.split(CHANNEL_NAME_SPLIT_DELIMITER)
                    splitNames.forEach { spl ->
                        add(
                            AvailableChannelParseModel(
                                channelId = chn.channelId,
                                channelIcon = chn.channelIcon,
                                channelNames = spl
                            )
                        )
                    }
                } else add(chn)
            }
        }
        insertChannelInfoAlterData(infoData = properList)
        preferenceRepository.setAlterInfoExist(state = true)
        Napier.i("testing updateChannelInfoAlterData complete ${properList.count()}")
    }

    private suspend fun updateChannelInfoMainData() {
        val infoResult = parseChannelInfoMainData()
        val filtered =
            infoResult.filter { it.id.isNotEmpty() && it.logo.isNotEmpty() }
        insertChannelInfoMainData(infoData = filtered)
        preferenceRepository.setMainInfoExist(state = true)
        Napier.i("testing updateChannelInfoMainData complete ${filtered.count()}")
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
                queries.deleteChannelInfoMainEntities()
                infoData.forEach { item ->
                    queries.insertChannelInfoMainEntity(
                        item.toChannelInfoMainEntity()
                    )
                }
            }
        }
    }

    private suspend fun insertChannelInfoAlterData(infoData: List<AvailableChannelParseModel>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.deleteChannelInfoAlterEntities()
                infoData.forEach { item ->
                    queries.insertChannelInfoAlterEntity(
                        item.toChannelInfoAlterEntity()
                    )
                }
            }
        }
    }

    private companion object {
        const val CHANNEL_NAME_SPLIT_DELIMITER = " • "
    }
}