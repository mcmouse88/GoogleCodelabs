package com.mcmouse88.common.media.extensions

import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.Charset

const val UTF_8 = "UTF-8"

/**
 * This file contains extension methods for the java.lang package.
 */

/**
 * Helper method to check if a [String] contains another in a case insensitive way.
 */
fun String?.containsCaseInsensitive(other: String?): Boolean {
    return when {
        this == null && other == null -> true
        this != null && other != null -> this.lowercase().contains(other.lowercase())
        else -> false
    }
}

/**
 * Helper extension to URL encode a [String]. Returns an empty String when called on null
 */
inline val String?.urlEncoded: String
    get() {
        this ?: return ""
        return if (Charset.isSupported(UTF_8)) {
            URLEncoder.encode(this, UTF_8)
        } else {
            // If UTF-8 is not supported, use the default charset.
            @Suppress("DEPRECATION")
            URLEncoder.encode(this)
        }
    }

/**
 * Helper extension to convert a potentially null [String] to a [Uri] falling back to [Uri.EMPTY]
 */
fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY