package com.mcmouse88.paging_advanced.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_advanced.model.UiModel

private const val REPO_TYPE = 0
private const val SEPARATOR_TYPE = 1

/**
 * Adapter for the list of repositories.
 */
class ReposAdapter(
    private val onItemClick: (String) -> Unit
) : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UI_MODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            REPO_TYPE -> RepoViewHolder.create(parent, inflater, onItemClick)
            SEPARATOR_TYPE -> SeparatorViewHolder.create(inflater, parent)
            else -> error("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is UiModel.RepoItem -> (holder as? RepoViewHolder)?.bind(uiModel.repo)
            is UiModel.SeparatorItem -> (holder as? SeparatorViewHolder)?.bind(uiModel.description)
            null -> error("Unknown Item")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> REPO_TYPE
            is UiModel.SeparatorItem -> SEPARATOR_TYPE
            null -> error("Unknown item type")
        }
    }

    companion object {
        private val UI_MODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {

            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                if (oldItem::class != newItem::class) return false
                return when (oldItem) {
                    is UiModel.RepoItem -> newItem is UiModel.RepoItem
                            && oldItem.repo.fullName == newItem.repo.fullName
                    is UiModel.SeparatorItem -> newItem is UiModel.SeparatorItem
                            && oldItem.description == newItem.description
                }
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}