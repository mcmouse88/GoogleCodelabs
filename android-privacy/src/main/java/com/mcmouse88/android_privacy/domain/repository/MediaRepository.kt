package com.mcmouse88.android_privacy.domain.repository

import com.mcmouse88.android_privacy.data.local.entity.LogEntry
import com.mcmouse88.android_privacy.domain.models.MediaEntry
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun fetchImages(): Flow<MediaEntry>
    suspend fun insert(log: LogEntry)
}
