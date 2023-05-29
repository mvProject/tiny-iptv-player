/*
 *  Created by Medvediev Viktor [mvproject]
 *  Copyright © 2023
 *  last modified : 10.05.23, 20:16
 *
 */

package com.mvproject.tinyiptv.di

import android.app.Application
import com.mvproject.tinyiptv.di.modules.appModule
import com.mvproject.tinyiptv.di.modules.helperModule
import com.mvproject.tinyiptv.di.modules.managerModule
import com.mvproject.tinyiptv.di.modules.networkModule
import com.mvproject.tinyiptv.di.modules.playerModule
import com.mvproject.tinyiptv.di.modules.repositoryModule
import com.mvproject.tinyiptv.di.modules.viewModelModule
import com.mvproject.tinyiptv.di.modules.workerModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            modules(
                appModule,
                playerModule,
                networkModule,
                repositoryModule,
                helperModule,
                managerModule,
                viewModelModule,
                workerModule
            )
        }
    }
}
