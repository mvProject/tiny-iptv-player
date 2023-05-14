/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.data.helpers.InfoChannelHelper
import com.mvproject.videoapp.data.helpers.PlaylistContentHelper
import com.mvproject.videoapp.data.helpers.ViewSettingsHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule = module {
    singleOf(::InfoChannelHelper)
    singleOf(::PlaylistContentHelper)
    singleOf(::ViewSettingsHelper)
}