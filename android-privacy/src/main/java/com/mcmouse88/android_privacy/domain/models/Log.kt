package com.mcmouse88.android_privacy.domain.models

import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

data class Log(
    val date: String,
    val place: String,
    val photos: List<File>
) {

    val timeInMillis = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)!!.time
}