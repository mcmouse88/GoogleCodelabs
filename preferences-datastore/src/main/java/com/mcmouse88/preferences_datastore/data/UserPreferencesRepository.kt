package com.mcmouse88.preferences_datastore.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}

data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
        // Note: this has the same name that we used with SharedPreferences.
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch {throwable ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }.map { preferences ->
            // Get the sort order from preferences and convert it to a [SortOrder] object
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )
            // Get our show completed value, defaulting to false if it not set
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            UserPreferences(showCompleted, sortOrder)
        }

    suspend fun enableSortByDeadline(enable: Boolean) {
        // edit handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            // Get the current SortOrder as an enum
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

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
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        // edit handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        dataStore.edit { preferences ->
            // Get the current SortOrder as an enum
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

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
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
    }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }
}