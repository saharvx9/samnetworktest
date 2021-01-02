package com.saharv.samnetworktest.application

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.saharv.samnetworktest.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application(){

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initAndroidThreeTen()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    //Configure AndroidThreeTen - Local Date
    private fun initAndroidThreeTen() {
        AndroidThreeTen.init(this)
    }
}