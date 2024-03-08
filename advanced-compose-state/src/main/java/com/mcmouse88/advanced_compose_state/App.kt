package com.mcmouse88.advanced_compose_state

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.compose.rememberImagePainter
import com.mcmouse88.advanced_compose_state.utils.UnsplashSizingInterceptor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    /**
     * Create the singleton [ImageLoader].
     * This is used by [rememberImagePainter] to load images in the app
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(UnsplashSizingInterceptor)
            }
            .build()
    }
}