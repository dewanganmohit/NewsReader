package com.myapp.application.newsreader.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.myapp.application.newsreader.R
import com.myapp.application.newsreader.databinding.FragmentArticleDetailsBinding
import com.myapp.application.newsreader.viewmodel.NewsViewModel
import com.myapp.application.newsreader.util.CommonUtils.Companion.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    private val newsViewModel: NewsViewModel by viewModels()
    val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentArticleDetailsBinding.bind(view)

        binding.apply {
            val article = args.article
            webView.apply {
                webViewClient = WebViewClient()
                article.url?.let {
                    loadUrl(article.url.toString())
                }
            }

            fab.setOnClickListener {
                newsViewModel.addToBookmark(article)
                view.showSnackBar(R.string.added_to_bookmark)
            }
        }
    }
}