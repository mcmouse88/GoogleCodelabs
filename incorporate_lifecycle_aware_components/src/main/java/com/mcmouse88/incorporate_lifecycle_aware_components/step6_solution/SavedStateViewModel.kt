package com.mcmouse88.incorporate_lifecycle_aware_components.step6_solution

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SavedStateViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val NAME_KEY = "name"
    }

    // Expose an immutable LiveData
    // getLiveData obtains an object that is associated with the key wrapped in a LiveData
    // so it can be observed for changes.
    val name: LiveData<String> get() = savedStateHandle.getLiveData(NAME_KEY)

    fun saveNewName(newName: String) {
        // Sets a new value for the object associated to the key. There's no need to set it
        // as a LiveData.
        savedStateHandle[NAME_KEY] = newName
    }
}
