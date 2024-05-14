package com.myapp.application.newsreader.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapp.application.newsreader.R
import com.myapp.application.newsreader.adapters.ArticlesAdapter
import com.myapp.application.newsreader.databinding.FragmentBookmarkedBinding
import com.myapp.application.newsreader.models.Article
import com.myapp.application.newsreader.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedFragment : Fragment(R.layout.fragment_bookmarked), ArticlesAdapter.OnItemClickListener {

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarkedBinding.bind(view)
        val articleAdapter = ArticlesAdapter(this)
        binding.apply {
            rvSavedNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val article = articleAdapter.currentList[viewHolder.adapterPosition]
                    newsViewModel.deleteArticle(article)
                    Snackbar.make(view, R.string.removed_from_bookmark, Snackbar.LENGTH_LONG)
                        .apply {
                            setAction(R.string.undo) {
                                newsViewModel.addToBookmark(article)
                            }
                            show()
                        }
                }
            }).attachToRecyclerView(rvSavedNews)
        }

        newsViewModel.getBookmarkedNews().observe(viewLifecycleOwner) {
            articleAdapter.submitList(it)
        }
    }

    override fun onItemClick(article: Article) {
        val action =
            BookmarkedFragmentDirections.actionBookmarkFragmentToArticleDetailsFragment(article)
        findNavController().navigate(action)
    }
}