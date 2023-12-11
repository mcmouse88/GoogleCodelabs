package com.mcmouse88.jetpack_compose_migration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mcmouse88.jetpack_compose_migration.data.GardenPlantingRepository
import com.mcmouse88.jetpack_compose_migration.data.PlantAndGardenPlantings

class GardenPlantingListViewModel internal constructor(
    gardenPlantingRepository: GardenPlantingRepository
) : ViewModel() {
    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
        gardenPlantingRepository.getPlantedGardens()
}
