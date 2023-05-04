/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:59
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:58
 *
 */

package com.mvproject.videoapp.data.models.parse

import com.mvproject.videoapp.utils.AppConstants

data class EpgProgramParseModel(
    val start: String,
    val stop: String,
    val channelId: String
) {
    var title: String = AppConstants.EMPTY_STRING
    var description: String = AppConstants.EMPTY_STRING
}