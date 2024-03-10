package com.mcmouse88.paging_basic.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_basic.Injection
import com.mcmouse88.paging_basic.databinding.ActivityArticleBinding
import kotlinx.coroutines.launch

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the ViewModel
        val viewModel by viewModels<ArticleViewModel>(
            factoryProducer = { Injection.provideViewModelFactory(owner = this) }
        )

        val adapter = ArticleAdapter()
        binding.bindAdapter(adapter)

        // Collect from the Article Flow in the ViewModel, and submit it to the ListAdapter.
        lifecycleScope.launch {
            // We repeat on the STARTED lifecycle because an Activity may be PAUSED
            // but still visible on the screen, for example in a multi window app
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.items.collect(adapter::submitList)
            }
        }
    }
}

/**
 * Sets up the [RecyclerView] and binds [ArticleAdapter] to it
 */
private fun ActivityArticleBinding.bindAdapter(adapter: ArticleAdapter) {
    rvArticles.adapter = adapter
    rvArticles.layoutManager = LinearLayoutManager(rvArticles.context)
    val decoration = DividerItemDecoration(rvArticles.context, DividerItemDecoration.VERTICAL)
    rvArticles.addItemDecoration(decoration)
}