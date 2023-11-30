package com.mcmouse88.cotlin_coroutines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.mcmouse88.cotlin_coroutines.fakes.MainNetworkFake
import com.mcmouse88.cotlin_coroutines.fakes.TitleDaoFake
import com.mcmouse88.cotlin_coroutines.main.MainViewModel
import com.mcmouse88.cotlin_coroutines.main.TitleRepository
import com.mcmouse88.cotlin_coroutines.utils.MainCoroutineScopeRule
import com.mcmouse88.cotlin_coroutines.utils.getValueForTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val coroutineScopeRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var subject: MainViewModel

    @Before
    fun setUp() {
        subject = MainViewModel(
            TitleRepository(
                network = MainNetworkFake("OK"),
                titleDao = TitleDaoFake("initial")
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenMainClicked_updateTaps() = runTest {
        subject.onMainViewClicked()
        Truth.assertThat(subject.taps.getValueForTest()).isEqualTo("0 taps")
        advanceUntilIdle()
        Truth.assertThat(subject.taps.getValueForTest()).isEqualTo("1 taps")
    }
}