package com.mcmouse88.advanced_compose_state.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mcmouse88.advanced_compose_state.base.Result
import com.mcmouse88.advanced_compose_state.data.DestinationsRepository
import com.mcmouse88.advanced_compose_state.data.ExploreModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cityName = savedStateHandle[KEY_ARG_DETAILS_CITY_NAME] ?: ""

    val cityDetails: Result<ExploreModel>
        get() {
            val destination = destinationsRepository.getDestination(cityName)
            return if (destination != null) {
                Result.Success(destination)
            } else {
                Result.Error(IllegalArgumentException("City doesn't exist"))
            }
        }
}
