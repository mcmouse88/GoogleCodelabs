package com.mcmouse88.kotlin_coroutines

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth
import com.mcmouse88.kotlin_coroutines.fakes.MainNetworkFake
import com.mcmouse88.kotlin_coroutines.main.RefreshMainDataWork
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RefreshMainDataWorkTest {

    @Test
    fun testRefreshMainDataWork() {
        val fakeNetwork = MainNetworkFake("OK")

        val context = ApplicationProvider.getApplicationContext<Context>()
        val worker = TestListenableWorkerBuilder<RefreshMainDataWork>(context)
            .setWorkerFactory(RefreshMainDataWork.Factory(fakeNetwork))
            .build()

        // start the work synchronously
        val result = worker.startWork().get()

        Truth.assertThat(result).isEqualTo(ListenableWorker.Result.success())
    }
}