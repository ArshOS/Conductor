package com.park.conductor.common.utilities

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        private lateinit var INSTANCE: App
        fun applicationContext(): Context = INSTANCE.applicationContext
        fun getINSTANCE(): App = INSTANCE
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    val getHostUrl: String
//        get() = if (BuildConfig.FLAVOR == Flavor.Dev) Urls.DevUrl else Urls.DevUrl
        get() = Urls.DevUrl
}
