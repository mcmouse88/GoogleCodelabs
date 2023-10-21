package com.mcmouse88.testing_basics.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mcmouse88.testing_basics.data.Result
import com.mcmouse88.testing_basics.data.Task
import com.mcmouse88.testing_basics.data.source.TaskDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class TasksLocalDataSource internal constructor(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskDataSource {

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return taskDao.observeTask().map { Result.Success(it) }
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(taskDao.getTasks())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun refreshTasks() {
        /* no-op */
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return taskDao.observeTaskById(taskId).map { Result.Success(it) }
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        return withContext(ioDispatcher) {
            try {
                val task = taskDao.getTaskById(taskId)
                if (task != null) {
                    return@withContext Result.Success(task)
                } else {
                    return@withContext Result.Error(Exception("Task not found!"))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
    }

    override suspend fun refreshTask(taskId: String) {
        /* no-op */
    }

    override suspend fun saveTask(task: Task) {
        return withContext(ioDispatcher) {
            taskDao.insertTask(task)
        }
    }

    override suspend fun completeTask(task: Task) {
        return withContext(ioDispatcher) {
            taskDao.updateCompleted(task.id, true)
        }
    }

    override suspend fun completeTask(taskId: String) {
        taskDao.updateCompleted(taskId, true)
    }

    override suspend fun activateTask(task: Task) {
        return withContext(ioDispatcher) {
            taskDao.updateCompleted(task.id, false)
        }
    }

    override suspend fun activateTask(taskId: String) {
        taskDao.updateCompleted(taskId, false)
    }

    override suspend fun clearCompletedTasks() {
        return withContext(ioDispatcher) {
            taskDao.deleteCompletedTasks()
        }
    }

    override suspend fun deleteAllTasks() {
        return withContext(ioDispatcher) {
            taskDao.deleteTasks()
        }
    }

    override suspend fun deleteTask(taskId: String) {
        return withContext(ioDispatcher) {
            taskDao.deleteTaskById(taskId)
        }
    }
}