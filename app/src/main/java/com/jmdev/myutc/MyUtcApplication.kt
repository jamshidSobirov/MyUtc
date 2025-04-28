package com.jmdev.myutc

import android.app.Application
import com.jmdev.myutc.di.appModule
import org.koin.core.context.startKoin

class MyUtcApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }

}