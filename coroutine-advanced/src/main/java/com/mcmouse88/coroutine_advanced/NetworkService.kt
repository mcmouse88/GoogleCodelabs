package com.mcmouse88.coroutine_advanced

import com.mcmouse88.sunflower.GrowZone
import com.mcmouse88.sunflower.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val sunflowersService = retrofit.create(SunflowerService::class.java)

    suspend fun allPlants(): List<Plant> = withContext(Dispatchers.Default) {
        val result = sunflowersService.getAllPlants()
        result.shuffled()
    }

    suspend fun plantsByGrowZone(growZone: GrowZone): List<Plant> = withContext(Dispatchers.Default) {
        val result = sunflowersService.getAllPlants()
        result.filter { it.growZoneNumber == growZone.number }.shuffled()
    }

    suspend fun customPlantSortOrder(): List<String> = withContext(Dispatchers.Default) {
        val result = sunflowersService.getCustomPlantSortOrder()
        result.map { plant -> plant.plantId }
    }
}

interface SunflowerService {

    @GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/plants.json")
    suspend fun getAllPlants(): List<Plant>

    @GET("googlecodelabs/kotlin-coroutines/master/advanced-coroutines-codelab/sunflower/src/main/assets/custom_plant_sort_order.json")
    suspend fun getCustomPlantSortOrder(): List<Plant>
}