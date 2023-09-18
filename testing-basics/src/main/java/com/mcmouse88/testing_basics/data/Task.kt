package com.mcmouse88.testing_basics.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Immutable model class for a Task. In order to compile with Room, we can't use @JvmOverloads to
 * generate multiple constructors.
 *
 * @param title       title of the task
 * @param description description of the task
 * @param isCompleted whether or not this task is completed
 * @param id          id of the task
 */
@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @[PrimaryKey ColumnInfo(name = "entryId")] var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "completed") var isCompleted: Boolean = false
) {

    val titleForList: String
        get() = title.ifEmpty { description }

    val isActive
        get() = isCompleted.not()

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}