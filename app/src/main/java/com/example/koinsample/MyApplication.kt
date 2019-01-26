package com.example.koinsample

import android.app.Application
import com.example.koinsample.di.appModules
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}
