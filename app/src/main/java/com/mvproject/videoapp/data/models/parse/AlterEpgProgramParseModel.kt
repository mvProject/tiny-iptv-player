/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.data.models.parse

import com.mvproject.videoapp.utils.AppConstants.EMPTY_STRING
import kotlinx.serialization.Serializable

@Serializable
data class AlterEpgProgramParseModel(
    val start: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val category: String = EMPTY_STRING
)