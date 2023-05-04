/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.mappers

import com.mvproject.videoapp.data.models.epg.EpgProgram
import com.mvproject.videoapp.data.models.parse.AlterEpgProgramParseModel
import com.mvproject.videoapp.data.models.parse.AvailableChannelParseModel
import com.mvproject.videoapp.data.models.parse.ChannelsInfoParseModel
import com.mvproject.videoapp.data.models.parse.EpgProgramParseModel
import com.mvproject.videoapp.utils.AppConstants.INT_VALUE_ZERO
import com.mvproject.videoapp.utils.AppConstants.LONG_NO_VALUE
import com.mvproject.videoapp.utils.AppConstants.LONG_VALUE_ZERO
import com.mvproject.videoapp.utils.TimeUtils.actualDate
import com.mvproject.videoapp.utils.dateEpgToIsoString
import com.mvproject.videoapp.utils.isoStringToMillis
import kotlinx.datetime.Instant
import videoappdb.ChannelInfoAlterEntity
import videoappdb.ChannelInfoMainEntity
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration.Companion.hours

object ParseMappers {
    fun EpgProgramParseModel.toEpgProgram() = with(this) {
        EpgProgram(
            start = start.dateEpgToIsoString().isoStringToMillis(),
            stop = stop.dateEpgToIsoString().isoStringToMillis(),
            channelId = channelId.toLong(),
            title = title,
            description = description
        )
    }

    fun ChannelsInfoParseModel.toChannelInfoMainEntity() = with(this) {
        ChannelInfoMainEntity(
            channelInfoId = id.toLong(),
            channelInfoName = title,
            channelInfoLogo = logo,
        )
    }

    fun AvailableChannelParseModel.toChannelInfoAlterEntity() = with(this) {
        ChannelInfoAlterEntity(
            channelId = channelId,
            channelInfoId = channelId.hashCode().toLong(),
            channelInfoName = channelNames,
            channelInfoLogo = channelIcon,
        )
    }

    /**
     * Maps List of [AlterEpgProgramParseModel] from Response to Entity
     * with id and filter for current day
     *
     * @param channelId id of channel for program
     *
     * @return the converted object
     */
    fun List<AlterEpgProgramParseModel>.asProgramEntities(
        channelId: Long
    ): List<EpgProgram> {
        val actualDayFiltered = this.filterToActualDay()

        val programEndings = actualDayFiltered
            .map { programResponse -> programResponse.start }
            .calculateEndings()

        return actualDayFiltered.mapIndexed { index, item ->
            val endingTime = programEndings.elementAtOrNull(index)
                ?: item.start.toMillis().getLastItemEnding()
            item.asProgramEntity(
                channelId = channelId,
                endTime = endingTime
            )
        }
    }


    /**
     * Maps List of [AlterEpgProgramParseModel] from Response to Entity.
     * with id and end time
     *
     * @param channelId id of channel for program
     * @param endTime time in milliseconds for program end
     *
     * @return the converted object
     */
    private fun AlterEpgProgramParseModel.asProgramEntity(
        channelId: Long,
        endTime: Long = LONG_VALUE_ZERO
    ) =
        with(this) {
            EpgProgram(
                start = start.toMillis(),
                stop = endTime,
                channelId = channelId,
                title = title,
                description = description
            )
        }

    /**
     * Filter List of [AlterEpgProgramParseModel] for current day programs start
     *
     * @return the filtered list
     */
    private fun List<AlterEpgProgramParseModel>.filterToActualDay(): List<AlterEpgProgramParseModel> {
        val actual = this.filter { programResponse ->
            programResponse.start.toMillis() > actualDate
        }
        return if (actual.count() > INT_VALUE_ZERO) actual else this
    }


    /**
     * Obtain time values of program end from start time of next
     *
     * @return the list of long values in milliseconds
     */
    fun List<String>.calculateEndings(): List<Long> {
        return buildList {
            this@calculateEndings.zipWithNext().forEach { timing ->
                add(timing.second.toMillis())
            }
        }
    }

    /**
     * Extension Method to non-null string variable which
     * convert date value to long value in milliseconds
     *
     * @return long value
     */
    fun String.toMillis(): Long {
        val parser = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return parser.parse(this)?.time ?: LONG_NO_VALUE
    }

    /**
     * Obtain time value of program end for last element
     * with hour duration from start time of current
     *
     * @return the long value in milliseconds
     */
    fun Long.getLastItemEnding() =
        (Instant.fromEpochMilliseconds(this) + NO_END_PROGRAM_DURATION.hours)
            .toEpochMilliseconds()

    private const val DATE_FORMAT = "dd-MM-yyyy HH:mm"
    private const val NO_END_PROGRAM_DURATION = 2
}