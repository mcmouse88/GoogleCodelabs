package com.mcmouse88.incorporate_lifecycle_aware_components.step5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A ViewModel used in step 5.
 */
class SeekBarViewModel : ViewModel() {

    val seekbarValue = MutableLiveData<Int>()
}
