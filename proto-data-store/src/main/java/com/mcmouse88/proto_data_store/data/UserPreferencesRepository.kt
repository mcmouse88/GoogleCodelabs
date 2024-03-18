package com.mcmouse88.proto_data_store.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import com.mcmouse88.proto_data_store.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val SORT_ORDER_KEY = "sort_order"

enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val userPreferencesStore: DataStore<UserPreferences>,
    context: Context
) {

    private val sharedPreferences = context.applicationContext.getSharedPreferences(
        USER_PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    /**
     * Get the sort order. By default, sort order is None.
     */
    private val sortOrder: SortOrder
        get() {
            val order = sharedPreferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
            return SortOrder.valueOf(order ?: SortOrder.NONE.name)
        }

    // Keep the sort order as a stream of changes
    private val _sortOrderFlow = MutableStateFlow(sortOrder)
    val sortOrderFlow: StateFlow<SortOrder> = _sortOrderFlow.asStateFlow()

    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { throwable ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (throwable is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", throwable)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw throwable
            }
        }

    fun enableSortByDeadline(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
        val newSortOrder = if (enable) {
            if (currentOrder == SortOrder.BY_PRIORITY) {
                SortOrder.BY_DEADLINE_AND_PRIORITY
            } else {
                SortOrder.BY_DEADLINE
            }
        } else {
            if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                SortOrder.BY_PRIORITY
            } else {
                SortOrder.NONE
            }
        }
        updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    fun enableSortByPriority(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
        val newSortOrder = if (enable) {
            if (currentOrder == SortOrder.BY_DEADLINE) {
                SortOrder.BY_DEADLINE_AND_PRIORITY
            } else {
                SortOrder.BY_PRIORITY
            }
        } else {
            if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                SortOrder.BY_DEADLINE
            } else {
                SortOrder.NONE
            }
        }
        updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    suspend fun updateShowCompleted(completed: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setShowCompleted(completed).build()
        }
    }

    private fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}