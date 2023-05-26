/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 04.05.23, 10:59
 *
 */

package com.mvproject.tinyiptv.di.modules

import com.mvproject.tinyiptv.data.helpers.InfoChannelHelper
import com.mvproject.tinyiptv.data.helpers.PlaylistContentHelper
import com.mvproject.tinyiptv.data.helpers.ViewSettingsHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val helperModule = module {
    singleOf(::InfoChannelHelper)
    singleOf(::PlaylistContentHelper)
    singleOf(::ViewSettingsHelper)
}