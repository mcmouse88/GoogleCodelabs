package com.mcmouse88.android_privacy.data.local.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore.Images.Media
import com.mcmouse88.android_privacy.data.local.dao.LogDao
import com.mcmouse88.android_privacy.data.local.entity.LogEntry
import com.mcmouse88.android_privacy.domain.models.MediaEntry
import com.mcmouse88.android_privacy.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logDao: LogDao
) : MediaRepository {

    override fun fetchImages(): Flow<MediaEntry> {
        return flow {
            val externalContentUri = Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                Media._ID,
                Media.DISPLAY_NAME,
                Media.SIZE,
                Media.MIME_TYPE,
                Media.DATA
            )

            val cursor = context.contentResolver.query(
                externalContentUri,
                projection,
                null,
                null,
                "${Media.DATE_ADDED} DESC"
            ) ?: error("Query could not be executed")

            cursor.use { cur ->
                while (cur.moveToNext()) {
                    val idColumn = cur.getColumnIndexOrThrow(Media._ID)
                    val displayNameColumn = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME)
                    val sizeColumn = cur.getColumnIndexOrThrow(Media.SIZE)
                    val mimeTypeColumn = cur.getColumnIndexOrThrow(Media.MIME_TYPE)
                    val dataColumn = cur.getColumnIndexOrThrow(Media.DATA)

                    val contentUri = ContentUris.withAppendedId(
                        externalContentUri,
                        cur.getLong(idColumn)
                    )

                    emit(
                        MediaEntry(
                            uri = contentUri,
                            filename = cur.getString(displayNameColumn),
                            mimeType = cur.getString(mimeTypeColumn),
                            size = cur.getLong(sizeColumn),
                            path = cur.getString(dataColumn)
                        )
                    )
                }
            }
        }
    }

    override suspend fun insert(log: LogEntry) {
        logDao.insert(log)
    }
}