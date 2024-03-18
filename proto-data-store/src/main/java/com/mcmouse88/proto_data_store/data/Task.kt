package com.mcmouse88.proto_data_store.data

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
