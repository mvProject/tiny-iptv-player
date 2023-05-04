/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:34
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.data.manager

import com.mvproject.videoapp.data.mappers.ParseMappers.asProgramEntities
import com.mvproject.videoapp.data.mappers.ParseMappers.toEpgProgram
import com.mvproject.videoapp.data.network.NetworkRepository
import com.mvproject.videoapp.data.parser.EpgParser
import com.mvproject.videoapp.data.repository.EpgInfoRepository
import com.mvproject.videoapp.data.repository.EpgProgramRepository
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class EpgManager(
    private val networkRepository: NetworkRepository,
    private val epgProgramRepository: EpgProgramRepository,
    private val epgInfoRepository: EpgInfoRepository
) {

    @OptIn(ExperimentalTime::class)
    suspend fun getMainEpgData() {
        val epgDataResult = networkRepository.loadEpgData()

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
        Napier.e("testing getEpg duration sec ${duration.inWholeSeconds}, min  ${duration.inWholeMinutes}")
    }


    suspend fun prepareEpgInfo() {
        epgInfoRepository.prepareEpgInfo()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getAlterEpg() {
        val alterData = epgInfoRepository.getEpgInfoByAlterIds()
        val duration = measureTime {
            alterData.forEach {
                val alterPrograms = try {
                    networkRepository
                        .loadAlterChannel(it.channelId)
                        .chPrograms
                } catch (exc: Exception) {
                    Napier.e("error ${exc.localizedMessage}")
                    emptyList()
                }
                if (alterPrograms.isNotEmpty()) {
                    val alterProgramsEntity = alterPrograms.asProgramEntities(it.channelAlterId)
                    withContext(Dispatchers.IO) {
                        epgProgramRepository.insertEpgPrograms(alterProgramsEntity)
                    }
                }
            }
        }
        Napier.e("testing getAlterEpg duration sec ${duration.inWholeSeconds}, min  ${duration.inWholeMinutes}")
    }
}











