package com.mcmouse88.accessibility_in_jetpack_compose.utils

internal fun <E> MutableSet<E>.addOrRemove(element: E) {
    if (!add(element)) {
        remove(element)
    }
}