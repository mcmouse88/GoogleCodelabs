package com.mcmouse88.paging_advanced.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ReposLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ReposLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ReposLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReposLoadStateViewHolder.create(inflater, parent, retry)
    }
}