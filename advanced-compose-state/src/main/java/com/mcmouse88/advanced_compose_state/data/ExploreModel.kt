package com.mcmouse88.advanced_compose_state.data

import androidx.compose.runtime.Immutable

@Immutable
data class ExploreModel(
    val city: City,
    val description: String,
    val imageUrl: String
)

@Immutable
data class City(
    val name: String,
    val country: String,
    val latitude: String,
    val longitude: String
) {
    val nameToDisplay = "$name, $country"
}
