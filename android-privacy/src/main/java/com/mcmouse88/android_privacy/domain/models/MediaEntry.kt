package com.mcmouse88.android_privacy.domain.models

import android.net.Uri
import java.io.File

data class MediaEntry(
    val uri: Uri,
    val filename: String,
    val mimeType: String,
    val size: Long,
    val path: String
) {
    val file: File
        get() = File(path)
}
