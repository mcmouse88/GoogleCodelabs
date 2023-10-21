package com.mcmouse88.testing_basics

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}