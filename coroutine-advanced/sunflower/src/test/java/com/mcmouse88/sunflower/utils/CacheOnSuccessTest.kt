package com.mcmouse88.sunflower.utils

import com.google.common.truth.Truth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CacheOnSuccessTest {

    @Test
    fun getOrAwait_caches() = runTest {
        var counter = 0
        val subject = CacheOnSuccess {
            counter++
        }

        subject.getOrAwait()
        Truth.assertThat(subject.getOrAwait()).isEqualTo(0)
        Truth.assertThat(counter).isEqualTo(1)
    }

    @Test
    fun getOrAwait_reentrant_caches() = runTest {
        val completable = CompletableDeferred<Unit>()
        var counter = 0

        val subject = CacheOnSuccess {
            completable.await()
            counter++
        }

        launch { subject.getOrAwait() }
        val actual = async { subject.getOrAwait() }
        completable.complete(Unit)

        Truth.assertThat(actual.await()).isEqualTo(0)
        Truth.assertThat(counter).isEqualTo(1)
    }

    @Test
    fun getOrAwait_throws() = runTest {
        val subject = CacheOnSuccess {
            throw SomeException()
        }

        val result = kotlin.runCatching { subject.getOrAwait() }
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(SomeException::class.java)
    }

    @Test
    fun getOrAwait_doesNotCacheException() = runTest {
        var counter = 0
        val subject = CacheOnSuccess {
            if (counter++ == 0) {
                throw SomeException()
            } else {
                counter
            }
        }

        kotlin.runCatching { subject.getOrAwait() }
        Truth.assertThat(subject.getOrAwait()).isEqualTo(2)
    }

    @Test
    fun getOrAwait_propagatesCancellation() = runTest {
        val subject = CacheOnSuccess {
            withTimeout(100) {
                delay(100)
            }
        }

        val result = kotlin.runCatching { subject.getOrAwait() }
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(CancellationException::class.java)
    }

    @Test(expected = TimeoutCancellationException::class)
    fun getOrAwait_propagatesCancellation_evenWithFallback() = runTest {
        var called = false
        val subject = CacheOnSuccess(onErrorFallback = { called = true; 5 }) {
            withTimeout(100) {
                delay(100)
            }
            1
        }

        val result = async { subject.getOrAwait() }
        advanceTimeBy(100)
        Truth.assertThat(called).isFalse()

        // and throw that
        result.await()
    }

    @Test
    fun getOrAwait_fallsBackOnException() = runTest {
        var calls = 0
        val subject = CacheOnSuccess(onErrorFallback = { calls++ }) {
            throw SomeException()
        }

        Truth.assertThat(subject.getOrAwait()).isEqualTo(0)
        Truth.assertThat(subject.getOrAwait()).isEqualTo(1)
        Truth.assertThat(calls).isEqualTo(2)
    }

    @Test
    fun getOrAwait_fallsBackOnException_withMultipleAwaits() = runTest {
        var calls = 0
        val subject = CacheOnSuccess(onErrorFallback = { calls++ }) {
            delay(100)
            throw SomeException()
        }

        launch { subject.getOrAwait() }
        launch { subject.getOrAwait() }
        launch { subject.getOrAwait() }

        advanceUntilIdle()

        Truth.assertThat(subject.getOrAwait()).isEqualTo(3)
        Truth.assertThat(calls).isEqualTo(4)
    }

    class SomeException : Throwable()
}