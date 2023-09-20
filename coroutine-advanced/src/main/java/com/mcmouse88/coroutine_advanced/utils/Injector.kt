package com.mcmouse88.coroutine_advanced.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.mcmouse88.coroutine_advanced.NetworkService
import com.mcmouse88.coroutine_advanced.PlantDao
import com.mcmouse88.coroutine_advanced.PlantRepository
import com.mcmouse88.coroutine_advanced.ui.PlantListViewModelFactory

interface ViewModelFactoryProvider {
    fun providePlantListViewModel(context: Context): PlantListViewModelFactory
}

val Injector: ViewModelFactoryProvider
    get() = currentInjector

private object DefaultViewModelProvider : ViewModelFactoryProvider {

    override fun providePlantListViewModel(context: Context): PlantListViewModelFactory {
        val repository = getPlantRepository(context)
        return PlantListViewModelFactory(repository)
    }

    private fun getPlantRepository(context: Context): PlantRepository {
        return PlantRepository.getInstance(
            plantDao(context),
            plantService()
        )
    }

    private fun plantService(): NetworkService = NetworkService()

    private fun plantDao(context: Context): PlantDao {
        return AppDatabase.getInstance(context).plantDao()
    }
}

private object Lock

@Volatile private var currentInjector: ViewModelFactoryProvider = DefaultViewModelProvider

@VisibleForTesting
private fun setInjectorForTesting(injector: ViewModelFactoryProvider?) {
    synchronized(Lock) {
        currentInjector = injector ?: DefaultViewModelProvider
    }
}

@VisibleForTesting
private fun resetInjector() {
    setInjectorForTesting(null)
}