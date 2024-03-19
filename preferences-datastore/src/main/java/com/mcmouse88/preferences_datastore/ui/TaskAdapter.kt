package com.mcmouse88.preferences_datastore.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mcmouse88.preferences_datastore.data.Task

class TaskAdapter : ListAdapter<Task, TaskViewHolder>(TASK_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val repoItem = getItem(position)
        holder.bind(repoItem)
    }

    companion object {
        private val TASK_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}