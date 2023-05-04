/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.models.response

import com.mvproject.videoapp.data.models.parse.AlterEpgProgramParseModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProgramListResponse(
    @SerialName("ch_programme")
    val chPrograms: List<AlterEpgProgramParseModel>
)