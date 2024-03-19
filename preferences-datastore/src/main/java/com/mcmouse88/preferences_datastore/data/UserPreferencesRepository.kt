package com.mcmouse88.preferences_datastore.data

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val SORT_ORDER_KEY = "sort_order"

enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}

data class UserPreferences(
    val showCompleted: Boolean
)

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    context: Context
) {

    private object PreferencesKeys {
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
    }

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

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch {throwable ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }.map { preferences ->
            // Get our show completed value, defaulting to false if it not set
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            UserPreferences(showCompleted)
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

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }

    private fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }
}