/*
 *  Created by Medvediev Viktor [mvproject] on 04.05.23, 10:47
 *  Copyright © 2023
 *  last modified : 03.05.23, 18:03
 *
 */

package com.mvproject.videoapp.di

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                appModule,
                playerModule,
                networkModule,
                repositoryModule,
                helperModule,
                managerModule,
                viewModelModule
            )
        }
    }
}
