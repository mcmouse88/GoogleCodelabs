package com.mcmouse88.kotlin_coroutines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.mcmouse88.kotlin_coroutines.fakes.MainNetworkCompletableFake
import com.mcmouse88.kotlin_coroutines.fakes.MainNetworkFake
import com.mcmouse88.kotlin_coroutines.fakes.TitleDaoFake
import com.mcmouse88.kotlin_coroutines.main.TitleRefreshError
import com.mcmouse88.kotlin_coroutines.main.TitleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TitleRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun whenRefreshTitleSuccess_insertsRows() = runTest {
        val titleDao = TitleDaoFake("title")
        val subject = TitleRepository(
            network = MainNetworkFake("OK"),
            titleDao = titleDao
        )
        subject.refreshTitle()
        Truth.assertThat(titleDao.nextInsertedOrNull()).isEqualTo("OK")
    }

    @Test(expected = TitleRefreshError::class)
    fun whenRefreshTitleTimeout_throwsTitleRefreshError() = runTest {
        val network = MainNetworkCompletableFake()
        val subject = TitleRepository(
            network = network,
            titleDao = TitleDaoFake("title")
        )
        launch { subject.refreshTitle() }
        advanceTimeBy(5_000)
    }
}