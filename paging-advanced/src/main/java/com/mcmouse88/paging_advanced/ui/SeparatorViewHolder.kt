package com.mcmouse88.paging_advanced.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_advanced.databinding.ItemSeparatorViewBinding

class SeparatorViewHolder(
    private val binding: ItemSeparatorViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(description: String) {
        binding.tvDescription.text = description
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): SeparatorViewHolder {
            val binding = ItemSeparatorViewBinding.inflate(inflater, parent, false)
            return SeparatorViewHolder(binding)
        }
    }
}