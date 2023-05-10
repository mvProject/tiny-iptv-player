/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:15
 *
 */

package com.mvproject.videoapp.data.repository

import com.mvproject.videoapp.VideoAppDatabase
import com.mvproject.videoapp.data.mappers.EntityMapper.toEpgProgram
import com.mvproject.videoapp.data.mappers.EntityMapper.toEpgProgramEntity
import com.mvproject.videoapp.data.models.epg.EpgProgram
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
        queries.transaction {
            queries.deleteEpgProgramsById(id = channel.channelId)
            queries.insertEpgProgram(
                channel.toEpgProgramEntity()
            )
        }
    }

    suspend fun insertEpgPrograms(programs: List<EpgProgram>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                val deleteIds = programs.map { it.channelId }.toSet()
                queries.deleteEpgProgramsByIds(ids = deleteIds)

                programs.forEach { prg ->
                    queries.insertEpgProgram(
                        prg.toEpgProgramEntity()
                    )
                }
            }
        }
    }
}