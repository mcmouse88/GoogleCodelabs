package com.mcmouse88.proto_data_store.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.recyclerview.widget.DividerItemDecoration
import com.mcmouse88.proto_data_store.UserPreferences
import com.mcmouse88.proto_data_store.data.SortOrder
import com.mcmouse88.proto_data_store.data.TasksRepository
import com.mcmouse88.proto_data_store.data.UserPreferencesRepository
import com.mcmouse88.proto_data_store.data.UserPreferencesSerializer
import com.mcmouse88.proto_data_store.databinding.ActivityTasksBinding

// https://developer.android.com/codelabs/android-proto-datastore?hl=en#0

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer
)

class TasksActivity : AppCompatActivity() {

    private var _binding: ActivityTasksBinding? = null
    val binding: ActivityTasksBinding
        get() = _binding ?: error("ActivityTasksBinding is null")

    private val viewModel by viewModels<TasksViewModel>(
        factoryProducer = {
            TasksViewModelFactory(
                repository = TasksRepository,
                userPreferencesRepository = UserPreferencesRepository(
                    userPreferencesStore = userPreferencesStore,
                    context = this
                )
            )
        }
    )

    private val adapter by lazy(LazyThreadSafetyMode.NONE) { TaskAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFilterListeners()
        setupSort()

        viewModel.tasksUiModel.observe(this) { task ->
            adapter.submitList(task.tasks)
            updateSort(task.sortOrder)
            binding.showCompletedSwitch.isChecked = task.showCompleted
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setupFilterListeners() {
        binding.showCompletedSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.showCompletedTasks(isChecked)
        }
    }

    private fun setupRecyclerView() {
        // Add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        binding.list.adapter = adapter
    }

    private fun setupSort() {
        binding.sortDeadline.setOnCheckedChangeListener { _, isChecked ->
            viewModel.enableSortByDeadline(isChecked)
        }

        binding.sortPriority.setOnCheckedChangeListener { _, isChecked ->
            viewModel.enableSortByPriority(isChecked)
        }
    }

    private fun updateSort(sortOrder: SortOrder) {
        binding.sortDeadline.isChecked = sortOrder == SortOrder.BY_DEADLINE
                || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        binding.sortPriority.isChecked = sortOrder == SortOrder.BY_PRIORITY
                || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
    }
}