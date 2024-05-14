package com.myapp.application.newsreader.repository

import com.myapp.application.newsreader.api.NewsApi
import com.myapp.application.newsreader.db.ArticleDao
import com.myapp.application.newsreader.models.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
) {

    suspend fun getHeadlines(countryCode: String, pagenumber: Int) =
        newsApi.getHeadlines(countryCode, pagenumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = articleDao.upsert(article)

    fun getBookmarkedNews() = articleDao.getAllArticle()

    suspend fun deleterArticle(article: Article) = articleDao.deleteArticle(article)
}