/*
 *  Created by Medvediev Viktor [mvproject] on 03.05.23, 18:06
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.data.models.parse

data class PlaylistChannelParseModel(
    val mStreamURL: String,
    val mLogoURL: String = "",
    val mGroupTitle: String = "",
    val mChannel: String = ""
)