package com.mcmouse88.incorporate_lifecycle_aware_components.step2

import androidx.lifecycle.ViewModel

/**
 * A ViewModel used for the {@link ChronoActivity2}.
 */
class ChronometerViewModel : ViewModel() {

    private var mStartTime: Long? = null

    fun getStartTime(): Long? {
        return mStartTime
    }

    fun setStartTime(startTime: Long) {
        this.mStartTime = startTime
    }
}
