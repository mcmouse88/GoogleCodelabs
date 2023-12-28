package com.mcmouse88.incorporate_lifecycle_aware_components.step6

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedStateViewModel : ViewModel() {

    // TODO: Create constructor and use the LiveData from SavedStateHandle.

    private val mName = MutableLiveData<String>()
    // Expose an immutable LiveData
    val name: LiveData<String> get() = mName

    fun saveNewName(newName: String) {
        mName.value = newName
    }
}
