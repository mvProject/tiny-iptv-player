/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.data.models.parse

import com.mvproject.tinyiptv.utils.AppConstants

data class EpgProgramParseModel(
    val start: String,
    val stop: String,
    val channelId: String
) {
    var title: String = AppConstants.EMPTY_STRING
    var description: String = AppConstants.EMPTY_STRING
}