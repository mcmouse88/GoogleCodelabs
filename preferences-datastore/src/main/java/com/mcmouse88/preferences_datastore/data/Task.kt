package com.mcmouse88.preferences_datastore.data

import java.util.Date

data class Task(
    val name: String,
    val deadline: Date,
    val priority: TaskPriority,
    val completed: Boolean = false
)

enum class TaskPriority {
    HIGH, MEDIUM, LOW
}