package com.mcmouse88.incorporate_lifecycle_aware_components.step3_solution

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

/**
 * A ViewModel used for the {@link ChronoActivity3}.
 */
class LiveDataTimerViewModel : ViewModel() {

    companion object {
        private const val ONE_SECOND = 1000L
    }

    private val mElapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long> get() = mElapsedTime

    private val mInitialTime: Long = SystemClock.elapsedRealtime()
    private val timer: Timer = Timer()

    init {
        // Update the elapsed time every second.
        timer.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {
                val newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000
                // setValue() cannot be called from a background thread so post to main thread.
                mElapsedTime.postValue(newValue)
            }
        }, ONE_SECOND, ONE_SECOND)

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}
