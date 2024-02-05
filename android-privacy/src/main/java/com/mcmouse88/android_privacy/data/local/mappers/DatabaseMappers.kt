package com.mcmouse88.android_privacy.data.local.mappers

import com.mcmouse88.android_privacy.data.local.entity.LogEntry
import com.mcmouse88.android_privacy.domain.models.Log
import java.io.File

fun Log.toLogEntry(): LogEntry {
    return LogEntry(
        date = this.date,
        place = this.place,
        photo1 = this.photos[0].name,
        photo2 = this.photos.getOrNull(1)?.name,
        photo3 = this.photos.getOrNull(2)?.name
    )
}

fun LogEntry.toLog(photoFolder: File): Log {
    return Log(
        date = this.date,
        place = this.place,
        photos = listOfNotNull(this.photo1, this.photo2, this.photo3).map {
            File(photoFolder, it)
        }
    )
}