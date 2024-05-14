package com.myapp.application.newsreader.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.application.newsreader.R
import com.myapp.application.newsreader.adapters.ArticlesAdapter
import com.myapp.application.newsreader.databinding.FragmentHeadlinesBinding
import com.myapp.application.newsreader.models.Article
import com.myapp.application.newsreader.viewmodel.NewsViewModel
import com.myapp.application.newsreader.util.CommonUtils.Companion.showToastMessage
import com.myapp.application.newsreader.util.Constants.Companion.COUNTRY_CODE
import com.myapp.application.newsreader.util.Constants.Companion.QUERY_PAGE_SIZE
import com.myapp.application.newsreader.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HeadlineFragment"

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines), ArticlesAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by viewModels()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHeadlinesBinding.bind(view)
        val articleAdapter = ArticlesAdapter(this)

        binding.apply {
            rvHeadlines.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlinesFragment.scrollListener)
            }
        }

        viewModel.headlines.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        articleAdapter.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.headlinesPage == totalPages
                        if (isLastPage)
                            binding.rvHeadlines.setPadding(0, 0, 0, 0)
                    }
                }

                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.message?.let { message ->
                        context?.showToastMessage(message)
                        Log.e(TAG, "Error: $message")
                    }
                }

                is Resource.Loading -> {
                    isLoading = true
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //State is scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getHeadlines(COUNTRY_CODE)
                isScrolling = false
            }
        }
    }

    override fun onItemClick(article: Article) {
        val action =
            HeadlinesFragmentDirections.actionHeadlineFragmentToArticleDetailsFragment(article)
        findNavController().navigate(action)
    }
}