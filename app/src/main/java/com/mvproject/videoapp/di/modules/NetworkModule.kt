/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 17:52
 *
 */

package com.mvproject.videoapp.di

import com.mvproject.videoapp.data.network.NetworkConnectivityObserver
import com.mvproject.videoapp.data.network.NetworkRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::NetworkRepository)
    singleOf(::NetworkConnectivityObserver)
}