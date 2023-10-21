package com.mcmouse88.testing_basics.data.source.remote

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.mcmouse88.testing_basics.data.Result
import com.mcmouse88.testing_basics.data.Task
import com.mcmouse88.testing_basics.data.source.TaskDataSource
import kotlinx.coroutines.delay

/**
 * Implementation of the data source that adds a latency simulating network.
 */
object TaskRemoteDataSource : TaskDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2_000L

    private var TASK_SERVICE_DATA = LinkedHashMap<String, Task>(2)

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    @SuppressLint("NullSafeMutableLiveData")
    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                Result.Loading -> Result.Loading
                is Result.Success -> {
                    val task = tasks.data.firstOrNull { it.id == taskId }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
                is Result.Error -> Result.Error(tasks.exception)
            }
        }
    }

    override suspend fun getTasks(): Result<List<Task>> {
        // Simulate network by delaying the exception
        val tasks = TASK_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(tasks)
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        // Simulate network by delaying the exception
        delay(SERVICE_LATENCY_IN_MILLIS)
        TASK_SERVICE_DATA[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Task not found"))
    }

    override suspend fun saveTask(task: Task) {
        TASK_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        TASK_SERVICE_DATA[task.id] = completedTask
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = task.copy(isCompleted = false)
        TASK_SERVICE_DATA[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        // Not required for the remote data source
    }

    override suspend fun clearCompletedTasks() {
        TASK_SERVICE_DATA = TASK_SERVICE_DATA.filterValues {
            it.isCompleted.not()
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteAllTasks() {
        TASK_SERVICE_DATA.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        TASK_SERVICE_DATA.remove(taskId)
    }

    private fun addTask(title: String, description: String) {
        val newTask = Task(title, description)
        TASK_SERVICE_DATA[newTask.id] = newTask
    }
}