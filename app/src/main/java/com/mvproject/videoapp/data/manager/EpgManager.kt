/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.manager

import com.mvproject.videoapp.data.helpers.InfoChannelHelper
import com.mvproject.videoapp.data.mappers.ParseMappers.asProgramEntities
import com.mvproject.videoapp.data.mappers.ParseMappers.toEpgProgram
import com.mvproject.videoapp.data.network.NetworkRepository
import com.mvproject.videoapp.data.parser.EpgParser
import com.mvproject.videoapp.data.repository.EpgInfoRepository
import com.mvproject.videoapp.data.repository.EpgProgramRepository
import com.mvproject.videoapp.data.repository.PreferenceRepository
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class EpgManager(
    private val networkRepository: NetworkRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgInfoRepository: EpgInfoRepository,
    private val infoChannelHelper: InfoChannelHelper,
    private val preferenceRepository: PreferenceRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend fun getMainEpgData() {
        val epgDataResult = networkRepository.loadEpgData()

        Napier.e("testing getMainEpgData start")
        val duration = measureTime {
            // EpgParser.parseTarGzEpg(
            //     data = epgDataResult,
            //     onProgramParsed = { prg ->
            //         val program = prg.toEpgProgram()
            //         Timber.e("currentProgram : $program")
            //         if (program.start >= actualDate) {
            //             withContext(Dispatchers.IO) {
            //                 epgProgramRepository.insertEpgProgram(program)
            //             }
            //         }
            //     }
            // )

            withContext(Dispatchers.IO) {
                EpgParser.parseTarGzEpg2(
                    data = epgDataResult,
                    onProgramParsed = { prg ->
                        val program = prg.toEpgProgram()
                        Napier.e("currentProgram : $program")
                        if (program.start >= actualDate) {
                            epgProgramRepository.insertEpgProgram2(program)
                        }
                    }
                )
            }
        }
        Napier.e("testing getMainEpgData duration sec ${duration.inWholeSeconds}, min  ${duration.inWholeMinutes}")
        preferenceRepository.setMainEpgLastUpdate(timestamp = actualDate)
    }


    suspend fun prepareEpgInfo() {
        epgInfoRepository.prepareEpgInfo()
        infoChannelHelper.checkAllPlaylistsChannelsInfo()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getAlterEpg() {
        Napier.e("testing getAlterEpg start")
        val alterData = epgInfoRepository.getEpgInfoByAlterIds()
        Napier.e("testing getAlterEpg alterDataCount:${alterData.count()}")
        val duration = measureTime {
            alterData.forEach { chn ->
                val alterPrograms = try {
                    networkRepository
                        .loadAlterChannel(chn.channelId)
                        .chPrograms
                } catch (exc: Exception) {
                    Napier.e("error ${exc.localizedMessage}")
                    emptyList()
                }
                if (alterPrograms.isNotEmpty()) {
                    val alterProgramsEntities = alterPrograms.asProgramEntities(chn.channelAlterId)

                    val actualAlterProgramsEntities = alterProgramsEntities
                        .filter { it.start > actualDate }

                    withContext(Dispatchers.IO) {
                        epgProgramRepository.insertEpgPrograms(actualAlterProgramsEntities)
                    }
                }
            }
        }
        Napier.e("testing getAlterEpg duration sec ${duration.inWholeSeconds}, min  ${duration.inWholeMinutes}")
        preferenceRepository.setAlterEpgLastUpdate(timestamp = actualDate)
    }
}











