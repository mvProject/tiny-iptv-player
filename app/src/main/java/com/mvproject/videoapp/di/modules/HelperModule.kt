/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.data.helpers.InfoChannelHelper
import com.mvproject.videoapp.data.helpers.PlaylistContentHelper
import com.mvproject.videoapp.data.helpers.ViewSettingsHelper
import com.mvproject.videoapp.utils.ContextUtil
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule = module {
    singleOf(::InfoChannelHelper)
    singleOf(::PlaylistContentHelper)
    singleOf(::ViewSettingsHelper)
    singleOf(::ContextUtil)
}