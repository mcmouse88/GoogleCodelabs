package com.mcmouse88.paging_basic.ui

import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_basic.data.Article
import com.mcmouse88.paging_basic.data.createdText
import com.mcmouse88.paging_basic.databinding.ItemArticleBinding

/**
 * View Holder for a [Article] RecyclerView list item.
 */
class ArticleViewHolder(
    private val binding: ItemArticleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        binding.apply {
            binding.title.text = article.title
            binding.description.text = article.description
            binding.created.text = article.createdText
        }
    }
}