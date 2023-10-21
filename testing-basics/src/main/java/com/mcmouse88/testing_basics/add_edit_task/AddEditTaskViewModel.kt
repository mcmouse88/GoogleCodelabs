package com.mcmouse88.testing_basics.add_edit_task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mcmouse88.testing_basics.Event
import com.mcmouse88.testing_basics.R
import com.mcmouse88.testing_basics.data.Result
import com.mcmouse88.testing_basics.data.Task
import com.mcmouse88.testing_basics.data.source.DefaultTaskRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add/Edit screen.
 */
class AddEditTaskViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val tasksRepository = DefaultTaskRepository.getRepository(application)

    // Two-way dataBinding, exposing MutableLiveData
    val title = MutableLiveData<String>()

    // Two-way dataBinding, exposing MutableLiveData
    val description = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _taskUpdateEvent = MutableLiveData<Event<Unit>>()
    val taskUpdateEvent: LiveData<Event<Unit>> = _taskUpdateEvent

    private var taskId: String? = null
    private var isNewTask: Boolean = false
    private var isDataLoaded: Boolean = false
    private var taskCompleted: Boolean = false

    fun start(taskId: String?) {
        if (_dataLoading.value == true) return
        this.taskId = taskId

        if (taskId == null) {
            // No need to populate, it's a new task
            isNewTask = true
            return
        }

        if (isDataLoaded) {
            // No need to populate, already have data
            return
        }

        isNewTask = false
        _dataLoading.value = true

        viewModelScope.launch {
            tasksRepository.getTask(taskId).let { result ->
                if (result is Result.Success) {
                    onTaskLoaded(result.data)
                } else {
                    onDataNotAvailable()
                }
            }
        }
    }

    // Called when clicking on fab
    fun saveTask() {
        val currentTitle = title.value
        val currentDescription = description.value

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        if (Task(currentTitle, currentDescription).isEmpty) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        val currentTaskId = taskId
        if (isNewTask || currentTaskId == null) {
            createTask(Task(currentTitle, currentDescription))
        } else {
            val task = Task(currentTaskId, currentTitle, currentDescription, taskCompleted)
            updateTask(task)
        }
    }

    private fun onTaskLoaded(task: Task) {
        title.value = task.title
        description.value = task.description
        taskCompleted = task.isCompleted
        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    private fun createTask(newTask: Task) = viewModelScope.launch {
        tasksRepository.saveTask(newTask)
        _taskUpdateEvent.value = Event(Unit)
    }

    private fun updateTask(task: Task) {
        if (isNewTask) {
            throw RuntimeException("updateTask() was called but task is new")
        }

        viewModelScope.launch {
            tasksRepository.saveTask(task)
            _taskUpdateEvent.value = Event(Unit)
        }
    }
}