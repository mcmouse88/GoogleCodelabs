package com.mcmouse88.advanced_compose_state.base

/**
 * A generic class that holds a value.
 * @param <T>
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}