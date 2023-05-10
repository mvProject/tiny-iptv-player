/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.data.enums.playlist

import androidx.annotation.StringRes
import com.mvproject.videoapp.R

enum class PlaylistUpdatePeriod(val value: Int, @StringRes val title: Int) {
    NO_UPDATE(0, R.string.playlist_update_never),
    HOURS_6(1, R.string.playlist_update_6_hours),
    HOURS_12(2, R.string.playlist_update_12_hours),
    HOURS_24(3, R.string.playlist_update_24_hours),
    DAYS_2(4, R.string.playlist_update_2_days),
    WEEK_1(5, R.string.playlist_update_1_week)
}