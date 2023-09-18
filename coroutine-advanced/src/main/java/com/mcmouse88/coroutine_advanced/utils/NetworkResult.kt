package com.mcmouse88.coroutine_advanced.utils

sealed class NetworkResult<out T>

// By using Nothing as T, Loading is a subtype of all NetworkResult<T>
data object Loading: NetworkResult<Nothing>()

// Successful result are stored in data
data class OK<out T>(val data: T): NetworkResult<T>()

// By using Nothing as T, all NetworkError instances are a subtype of all NetworkResult<T>
data class NetworkError(val exception: Throwable): NetworkResult<Nothing>()