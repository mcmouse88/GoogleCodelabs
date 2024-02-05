package com.mcmouse88.android_privacy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mcmouse88.android_privacy.data.local.entity.LogEntry
import com.mcmouse88.android_privacy.data.local.mappers.toLog
import com.mcmouse88.android_privacy.domain.models.Log
import java.io.File

@Dao
interface LogDao {

    @Query("SELECT * FROM logs ORDER BY date DESC")
    suspend fun getAll(): List<LogEntry>

    suspend fun getAllWithFiles(photoFolder: File): List<Log> {
        return getAll().map { it.toLog(photoFolder) }
    }

    @Upsert
    suspend fun insert(log: LogEntry)

    @Delete
    suspend fun delete(log: LogEntry)
}
