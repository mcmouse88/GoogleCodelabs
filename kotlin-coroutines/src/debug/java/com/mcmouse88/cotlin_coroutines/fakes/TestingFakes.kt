package com.mcmouse88.cotlin_coroutines.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcmouse88.cotlin_coroutines.main.MainNetwork
import com.mcmouse88.cotlin_coroutines.main.Title
import com.mcmouse88.cotlin_coroutines.main.TitleDao
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fake [TitleDao] for use in tests.
 */
class TitleDaoFake(initialTitle: String): TitleDao {

    /**
     * A channel is a Coroutines based implementation of a blocking queue.
     *
     * We're using it here as a buffer of inserted elements.
     *
     * This uses a channel instead of a list to allow multiple threads to call insertTitle and
     * synchronize the results with the test thread.
     */
    private val insertedForNext = Channel<Title>(capacity = Channel.BUFFERED)

    private val _titleLiveData = MutableLiveData(Title(initialTitle))
    override val titleLiveData: LiveData<Title?> get() = _titleLiveData

    override suspend fun insertTitle(title: Title) {
        insertedForNext.trySend(title)
        _titleLiveData.value = title
    }

    /**
     * Assertion that the next element inserted has a title of expected
     *
     * If the element was previously inserted and is currently the most recent element
     * this assertion will also match. This allows tests to avoid synchronizing calls to insert
     * with calls to assertNextInsert.
     *
     * If multiple items were inserted, this will always match the first item that was not
     * previously matched.
     *
     * @param expected the value to match
     * @param timeout duration to wait (this is provided for instrumentation tests that may run on
     *                multiple threads)
     * @param unit timeunit
     * @return the next value that was inserted into this dao, or null if none found
     */
    fun nextInsertedOrNull(timeout: Long = 2_000): String? {
        var result: String? = null
        runBlocking {
            // wait for the next insertion to complete
            try {
                withTimeout(timeout){
                    result = insertedForNext.receive().title
                }
            } catch (ex: TimeoutCancellationException) {
                // ignore
            }
        }
        return result
    }
}

/**
 * Testing Fake implementation of MainNetwork
 */
class MainNetworkFake(var result: String): MainNetwork {
    override suspend fun fetchNextTitle(): String = result
}

/**
 * Testing Fake for MainNetwork that lets you complete or error all current requests
 */
class MainNetworkCompletableFake(): MainNetwork {

    private var completable = CompletableDeferred<String>()

    override suspend fun fetchNextTitle(): String = completable.await()

    fun sendCompletionToAllCurrentRequests(result: String) {
        completable.complete(result)
        completable = CompletableDeferred()
    }

    fun sendErrorToCurrentRequests(throwable: Throwable) {
        completable.completeExceptionally(throwable)
        completable = CompletableDeferred()
    }
}

typealias MakeCompilerHappyForStartedCode = FakeCallForRetrofit<String>

/**
 * This class only exists to make the starter code compile. Remove after refactoring retrofit to use
 * suspend functions.
 */
class FakeCallForRetrofit<T> : Call<T> {

    override fun enqueue(callback: Callback<T>) {
        // nothing
    }

    override fun isExecuted(): Boolean = false

    override fun clone(): Call<T> = this

    override fun isCanceled(): Boolean = true

    override fun cancel() {
        // nothing
    }

    override fun execute(): Response<T> {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }
}