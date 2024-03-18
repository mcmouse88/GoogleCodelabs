package com.mcmouse88.proto_data_store.data

import android.util.Log
import androidx.datastore.core.DataStore
import com.mcmouse88.proto_data_store.SortOrderOuterClass.SortOrder
import com.mcmouse88.proto_data_store.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository(
    private val userPreferencesStore: DataStore<UserPreferences>
) {

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

    suspend fun enableSortByDeadline(enable: Boolean) {
        // updateData handles data transactionally, ensuring that if the sort is updated at the same
        // time from another thread, we won't have conflicts
        userPreferencesStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder
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
            preferences.toBuilder().setSortOrder(newSortOrder).build()
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        userPreferencesStore.updateData { preferences ->
            val currentOrder = preferences.sortOrder
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
            preferences.toBuilder().setSortOrder(newSortOrder).build()
        }
    }

    suspend fun updateShowCompleted(completed: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setShowCompleted(completed).build()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}