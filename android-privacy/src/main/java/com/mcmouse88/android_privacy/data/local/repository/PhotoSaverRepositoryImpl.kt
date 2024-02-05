package com.mcmouse88.android_privacy.data.local.repository

import android.content.Context
import android.net.Uri
import com.mcmouse88.android_privacy.data.local.entity.MAX_LOG_PHOTOS_LIMIT
import com.mcmouse88.android_privacy.domain.repository.PhotoSaverRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoSaverRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PhotoSaverRepository {

    private val _photos = mutableListOf<File>()
    private val contentResolver = context.contentResolver

    private val cacheFolder = File(context.cacheDir, "photos").also { it.mkdir() }
    val photoFolder = File(context.filesDir, "photos").also { it.mkdir() }

    override fun cacheCapturedPhoto(photo: File) {
        if (_photos.size + 1 > MAX_LOG_PHOTOS_LIMIT) return
        _photos += photo
    }

    override suspend fun cacheFromUri(uri: Uri) {
        withContext(Dispatchers.IO) {
            if (_photos.size + 1 > MAX_LOG_PHOTOS_LIMIT) {
                return@withContext
            }

            contentResolver.openInputStream(uri)?.use { input ->
                val cachedPhoto = generatePhotoCacheFile()

                cachedPhoto.outputStream().use { output ->
                    input.copyTo(output)
                    _photos += cachedPhoto
                }
            }
        }
    }

    override suspend fun cacheFromUris(uris: List<Uri>) {
        uris.forEach {
            cacheFromUri(it)
        }
    }

    override fun getPhotos(): List<File> {
        return _photos.toList()
    }

    override suspend fun removeFile(photo: File) {
        withContext(Dispatchers.IO) {
            photo.delete()
            _photos -= photo
        }
    }

    override suspend fun savePhotos(): List<File> {
        return withContext(Dispatchers.IO) {
            val savedPhotos = _photos.map { it.copyTo(generatePhotoLogFile()) }

            _photos.forEach { it.delete() }
            _photos.clear()
            savedPhotos
        }
    }

    private fun generatePhotoCacheFile(): File {
        return File(cacheFolder, generateFileName())
    }

    private fun generateFileName(): String {
        return "${System.currentTimeMillis()}.jpg"
    }

    private fun generatePhotoLogFile(): File {
        return File(photoFolder, generateFileName())
    }
}