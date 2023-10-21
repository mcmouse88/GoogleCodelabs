package com.mcmouse88.testing_basics.tasks

import android.app.Application
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mcmouse88.testing_basics.Event
import com.mcmouse88.testing_basics.R
import com.mcmouse88.testing_basics.data.Result
import com.mcmouse88.testing_basics.data.Task
import com.mcmouse88.testing_basics.data.source.DefaultTaskRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the task list screen.
 */
class TasksViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val taskRepository = DefaultTaskRepository.getRepository(application)

    private val _forceUpdate = MutableLiveData(false)

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _items: LiveData<List<Task>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                taskRepository.refreshTasks()
                _dataLoading.value = false
            }
        }
        taskRepository.observeTasks().switchMap { filterTasks(it) }
    }

    val items: LiveData<List<Task>> = _items

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel:LiveData<Int> = _currentFilteringLabel

    private val _noTasksLabel = MutableLiveData<Int>()
    val noTasksLabel: LiveData<Int> = _noTasksLabel

    private val _noTaskIconRes = MutableLiveData<Int>()
    val noTaskIconRes: LiveData<Int> = _noTaskIconRes

    private val _tasksAndViewVisible = MutableLiveData<Boolean>()
    val tasksAndViewVisible: LiveData<Boolean> = _tasksAndViewVisible

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var currentFiltering = TasksFilterType.ALL_TASKS

    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>> = _newTaskEvent

    private var resultMessageShown: Boolean = false

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = _items.map { it.isEmpty() }

    init {
        // Set initial state
        setFiltering(TasksFilterType.ALL_TASKS)
        loadTasks(true)
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be [TasksFilterType.ALL_TASKS],
     * [TasksFilterType.COMPLETED_TASKS], or
     * [TasksFilterType.ACTIVE_TASKS]
     */
    fun setFiltering(requestType: TasksFilterType) {
        currentFiltering = requestType

        // Depending on the filter type, set the filtering label, icon drawable, etc.
        when (requestType) {
            TasksFilterType.ALL_TASKS -> {
                setFilter(
                    filteringLabelString = R.string.label_all,
                    noTasksLabelString = R.string.no_tasks_all,
                    noTaskIconDrawable = R.drawable.logo_no_fill,
                    tasksAddVisible = true
                )
            }
            TasksFilterType.ACTIVE_TASKS -> {
                setFilter(
                    filteringLabelString = R.string.label_active,
                    noTasksLabelString = R.string.no_tasks_active,
                    noTaskIconDrawable = R.drawable.ic_check_circle_96dp,
                    tasksAddVisible = false
                )
            }
            TasksFilterType.COMPLETED_TASKS -> {
                setFilter(
                    filteringLabelString = R.string.label_completed,
                    noTasksLabelString = R.string.no_tasks_completed,
                    noTaskIconDrawable = R.drawable.ic_verified_user_96dp,
                    tasksAddVisible = false
                )
            }
        }

        // Refresh list
        loadTasks(false)
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            taskRepository.clearCompletedTasks()
            showSnackbarMessage(R.string.completed_tasks_cleared)
        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            taskRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            taskRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    fun addNewTask() {
        _newTaskEvent.value = Event(Unit)
    }

    /**
     * Called by Data Binding.
     */
    fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    fun showEditResultMessage(result: Int) {
        if (resultMessageShown) return
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
        resultMessageShown = true
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     */
    fun loadTasks(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun refresh() {
        _forceUpdate.value = true
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int,
        @StringRes noTasksLabelString: Int,
        @DrawableRes noTaskIconDrawable: Int,
        tasksAddVisible: Boolean
    ) {
        _currentFilteringLabel.value = filteringLabelString
        _noTasksLabel.value = noTasksLabelString
        _noTaskIconRes.value = noTaskIconDrawable
        _tasksAndViewVisible.value = tasksAddVisible
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun filterTasks(tasksResult: Result<List<Task>>): LiveData<List<Task>> {
        // TODO: This is a good case for LiveData builder. Replace when stable
        val result = MutableLiveData<List<Task>>()

        if (tasksResult is Result.Success) {
            isDataLoadingError.value = false
            viewModelScope.launch {
                result.value = filterItems(tasksResult.data, currentFiltering)
            }
        } else {
            result.value = emptyList()
            showSnackbarMessage(R.string.loading_tasks_error)
            isDataLoadingError.value = true
        }

        return result
    }

    private fun filterItems(tasks: List<Task>, filterType: TasksFilterType): List<Task> {
        val tasksToShow = ArrayList<Task>()
        // We filter the tasks based on the request type
        for (task in tasks) {
            when (filterType) {
                TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                TasksFilterType.ACTIVE_TASKS -> if (task.isActive) tasksToShow.add(task)
                TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) tasksToShow.add(task)
            }
        }
        return tasksToShow
    }
}