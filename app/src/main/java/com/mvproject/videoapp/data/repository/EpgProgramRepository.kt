/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.repository

import com.mvproject.videoapp.VideoAppDatabase
import com.mvproject.videoapp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.videoapp.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EpgProgramRepository(private val db: VideoAppDatabase) {

    private val queries = db.epgProgramQueries

    fun getEpgProgramsById(channelId: Long, time: Long): List<EpgProgram> {
        return queries.getEpgProgramsById(
            id = channelId,
            time = time
        ).executeAsList().map {
            it.toEpgProgram()
        }
    }

    fun getEpgProgramsByIdWithLimit(channelId: Long, limit: Long, time: Long): List<EpgProgram> {
        return queries.getEpgProgramsByIdWithLimit(
            id = channelId,
            time = time,
            limit = limit
        ).executeAsList().map {
            it.toEpgProgram()
        }
    }

    fun getEpgProgramsByIds(channelIds: List<Long>, time: Long): List<EpgProgram> {
        return queries.getEpgProgramsByIds(channelIds, time)
            .executeAsList()
            .map {
                it.toEpgProgram()
            }
    }

    // fun getEpgPrograms(): List<EpgProgram> {
    //     return queries.getEpgPrograms().executeAsList().map {
    //         it.toEpgProgram()
    //     }
    // }


    suspend fun deleteEpgProgramsByIdTime(id: Long, time: Long) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.deleteEpgProgramsByIdTime(id, time)
            }
        }
    }

    suspend fun insertEpgProgram(channel: EpgProgram) {
        withContext(Dispatchers.IO) {
            queries.insertEpgProgram(
                channel.toEpgProgramEntity()
            )
        }
    }

    fun insertEpgProgram2(channel: EpgProgram) {
        queries.insertEpgProgram(
            channel.toEpgProgramEntity()
        )
    }

    suspend fun insertEpgPrograms(programs: List<EpgProgram>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                programs.forEach { prg ->
                    if (prg.start >= actualDate) {
                        queries.deleteEpgProgramsById(id = prg.channelId)
                        queries.insertEpgProgram(
                            prg.toEpgProgramEntity()
                        )
                    }
                }
            }
        }
    }
}