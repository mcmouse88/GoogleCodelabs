package com.mcmouse88.proto_data_store.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.proto_data_store.R
import com.mcmouse88.proto_data_store.data.Task
import com.mcmouse88.proto_data_store.data.TaskPriority
import com.mcmouse88.proto_data_store.databinding.ItemTasksBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Holder for a task item in the tasks list
 */
class TaskViewHolder(
    private val binding: ItemTasksBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Format date as: Apr6, 2020
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

    /**
     * Bind the task to the UI elements
     */
    fun bind(todo: Task) {
        binding.task.text = todo.name
        setTaskPriority(todo)
        binding.deadline.text = dateFormat.format(todo.deadline)
        // If a task was completed, show it grayed out
        val color = if (todo.completed) {
            R.color.greyAlpha
        } else {
            R.color.white
        }

        itemView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                color
            )
        )
    }

    private fun setTaskPriority(todo: Task) {
        binding.priority.text = itemView.context.resources.getString(
            R.string.priority_value,
            todo.priority.name
        )

        // set the priority color based on the task priority
        val textColor = when (todo.priority) {
            TaskPriority.HIGH -> R.color.red
            TaskPriority.MEDIUM -> R.color.yellow
            TaskPriority.LOW -> R.color.green
        }

        binding.priority.setTextColor(ContextCompat.getColor(itemView.context, textColor))
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): TaskViewHolder {
            val binding = ItemTasksBinding.inflate(inflater, parent, false)
            return TaskViewHolder(binding)
        }
    }
}