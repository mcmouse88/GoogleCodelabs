package com.mcmouse88.android_privacy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mcmouse88.android_privacy.data.local.dao.LogDao
import com.mcmouse88.android_privacy.data.local.entity.LogEntry

@Database(entities = [LogEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        const val DB_NAME = "main.db"
    }
}