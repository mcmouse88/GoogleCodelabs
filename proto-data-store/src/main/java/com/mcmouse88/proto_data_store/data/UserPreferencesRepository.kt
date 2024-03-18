package com.mcmouse88.proto_data_store.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
class UserPreferencesRepository private constructor(context: Context) {

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

    private fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = UserPreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}