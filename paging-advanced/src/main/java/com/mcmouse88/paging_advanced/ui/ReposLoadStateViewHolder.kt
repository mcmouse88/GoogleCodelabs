package com.mcmouse88.paging_advanced.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_advanced.databinding.ItemLoadStateFooterBinding

class ReposLoadStateViewHolder(
    private val binding: ItemLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        with(binding) {
            if (loadState is LoadState.Error) {
                tvErrorMessage.text = loadState.error.localizedMessage
            }

            progressBar.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState is LoadState.Error
            tvErrorMessage.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            retry: () -> Unit
        ): ReposLoadStateViewHolder {
            val binding = ItemLoadStateFooterBinding.inflate(inflater, parent, false)
            return ReposLoadStateViewHolder(binding, retry)
        }
    }
}