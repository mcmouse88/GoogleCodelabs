package com.mcmouse88.paging_advanced.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_advanced.R
import com.mcmouse88.paging_advanced.databinding.ItemRepoViewBinding
import com.mcmouse88.paging_advanced.model.Repo

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(
    private val binding: ItemRepoViewBinding,
    private val onItemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo) {
        with(binding) {
            repoName.text = repo.fullName

            repoDescription.isVisible = repo.description != null
            repoDescription.text = repo.description

            repoStars.text = repo.stars.toString()
            repoForks.text = repo.forks.toString()

            repoLanguage.isVisible = !repo.language.isNullOrEmpty()
            repoLanguage.text = binding.root.resources.getString(R.string.language, repo.language)

            root.setOnClickListener {
                onItemClick.invoke(repo.url)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            inflater: LayoutInflater,
            onItemClick: (String) -> Unit
        ): RepoViewHolder {
            val binding = ItemRepoViewBinding.inflate(inflater, parent, false)
            return RepoViewHolder(binding, onItemClick)
        }
    }
}