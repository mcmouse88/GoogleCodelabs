package com.mcmouse88.android_privacy.domain.repository

import android.net.Uri
import java.io.File

interface PhotoSaverRepository {
    fun cacheCapturedPhoto(photo: File)
    suspend fun cacheFromUri(uri: Uri)
    suspend fun cacheFromUris(uris: List<Uri>)
    fun getPhotos(): List<File>
    suspend fun removeFile(photo: File)
    suspend fun savePhotos(): List<File>
}