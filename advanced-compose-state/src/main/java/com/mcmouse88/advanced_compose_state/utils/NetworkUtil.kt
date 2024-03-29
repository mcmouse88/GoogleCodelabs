package com.mcmouse88.advanced_compose_state.utils

import coil.intercept.Interceptor
import coil.request.ImageResult
import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * A Coil [Interceptor] which appends query params to Unsplash urls to request sized imaged
 */
object UnsplashSizingInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val data = chain.request.data
        val size = chain.size
        if (data is String && data.startsWith("https://images.unsplash.com/photo-")) {
            val url = data.toHttpUrl()
                .newBuilder()
                .addQueryParameter("w", size.width.toString())
                .addQueryParameter("h", size.height.toString())
                .build()
            val request = chain.request.newBuilder().data(url).build()
            return chain.proceed(request)
        }
        return chain.proceed(chain.request)
    }
}