package com.mcmouse88.testing_basics.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mcmouse88.testing_basics.data.Result
import com.mcmouse88.testing_basics.data.Task
import com.mcmouse88.testing_basics.data.source.DefaultTaskRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the statistics screen.
 */
class StatisticsViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val tasksRepository = DefaultTaskRepository.getRepository(application)

    private val tasks: LiveData<Result<List<Task>>> = tasksRepository.observeTasks()

    private val _dataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val stats: LiveData<StatsResult?> = tasks.map {
        if (it is Result.Success) {
            getActiveAndCompletedStats(it.data)
        } else {
            null
        }
    }

    val activeTasksPercent: LiveData<Float> = stats.map { it?.activeTasksPercent ?: 0f }
    val completedTasksPercent: LiveData<Float> = stats.map { it?.completedTaskPercent ?: 0f }
    val error: LiveData<Boolean> = tasks.map { it is Result.Error }
    val empty: LiveData<Boolean> = tasks.map { (it as? Result.Success)?.data.isNullOrEmpty() }

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.refreshTasks()
            _dataLoading.value = false
        }
    }
}