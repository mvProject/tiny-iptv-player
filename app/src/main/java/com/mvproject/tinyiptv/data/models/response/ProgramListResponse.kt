/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.response

import com.mvproject.tinyiptv.data.models.parse.AlterEpgProgramParseModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProgramListResponse(
    @SerialName("ch_programme")
    val chPrograms: List<AlterEpgProgramParseModel>
)