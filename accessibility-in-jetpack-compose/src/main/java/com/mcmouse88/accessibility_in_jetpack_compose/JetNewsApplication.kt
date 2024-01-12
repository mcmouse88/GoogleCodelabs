package com.mcmouse88.accessibility_in_jetpack_compose

import android.app.Application
import com.mcmouse88.accessibility_in_jetpack_compose.data.AppContainer
import com.mcmouse88.accessibility_in_jetpack_compose.data.AppContainerImpl

class JetNewsApplication : Application() {

    // AppContainer instance used by the rest of classes of obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}