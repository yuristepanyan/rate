package com.rate.am

import android.app.Application
import com.rate.am.di.adapterModule
import com.rate.am.di.networkModule
import com.rate.am.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RateApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@RateApp)
            modules(listOf(networkModule, viewModelModule, adapterModule))
        }
    }
}