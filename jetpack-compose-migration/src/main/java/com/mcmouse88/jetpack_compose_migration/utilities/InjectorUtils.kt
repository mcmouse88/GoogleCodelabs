package com.mcmouse88.jetpack_compose_migration.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.mcmouse88.jetpack_compose_migration.data.AppDatabase
import com.mcmouse88.jetpack_compose_migration.data.GardenPlantingRepository
import com.mcmouse88.jetpack_compose_migration.data.PlantRepository
import com.mcmouse88.jetpack_compose_migration.viewmodels.GardenPlantingListViewModelFactory
import com.mcmouse88.jetpack_compose_migration.viewmodels.PlantDetailViewModelFactory
import com.mcmouse88.jetpack_compose_migration.viewmodels.PlantListViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getPlantRepository(context: Context): PlantRepository {
        return PlantRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).plantDao()
        )
    }

    private fun getGardenPlantingRepository(context: Context): GardenPlantingRepository {
        return GardenPlantingRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).gardenPlantingDao()
        )
    }

    fun provideGardenPlantingListViewModelFactory(
        context: Context
    ): GardenPlantingListViewModelFactory {
        return GardenPlantingListViewModelFactory(getGardenPlantingRepository(context))
    }

    fun providePlantListViewModelFactory(fragment: Fragment): PlantListViewModelFactory {
        return PlantListViewModelFactory(getPlantRepository(fragment.requireContext()), fragment)
    }

    fun providePlantDetailViewModelFactory(
        context: Context,
        plantId: String
    ): PlantDetailViewModelFactory {
        return PlantDetailViewModelFactory(
            getPlantRepository(context),
            getGardenPlantingRepository(context),
            plantId
        )
    }
}
