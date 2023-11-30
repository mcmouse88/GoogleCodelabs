package com.mcmouse88.main_app.ui

import android.app.Application
import androidx.compose.ui.util.trace
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BenchMarkApplication : Application() {

    private fun initializeLibrary() {
        trace("Custom Library Init") {
            runBlocking {
                delay(50L)
            }
        }
    }
}