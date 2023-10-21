package com.mcmouse88.testing_basics.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcmouse88.testing_basics.data.Task

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}