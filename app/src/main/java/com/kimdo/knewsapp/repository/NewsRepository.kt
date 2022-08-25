package com.kimdo.knewsapp.repository


import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kimdo.knewsapp.api.NewsApi
import com.kimdo.knewsapp.db.ArticleDao
import com.kimdo.knewsapp.models.Article
import com.kimdo.knewsapp.models.NewsResponse
import com.kimdo.knewsapp.util.Resource
import kotlinx.coroutines.flow.Flow

class NewsRepository (
    private val api: NewsApi,
    private val dao: ArticleDao
        ) {

    suspend fun getBreakingNews(
        countryCode: String = "us",
        pageNumber: Int = 1
    ): Resource<NewsResponse> {
        val response = try {
            api.getBreakingNews( countryCode, pageNumber)
        } catch(e: Exception) {
            return Resource.Error(message = "error iis occurred..")
        }
        return Resource.Success(data = response)
    }

    suspend fun searchForNews(
        searchQuery: String,
        pageNumber: Int = 1
    ): Resource<NewsResponse> {

        val response = try {
            api.searchForNews(searchQuery, pageNumber)
        } catch(e: Exception) {
            return Resource.Error(message = "error iis occurred..")
        }
        return Resource.Success(data = response)
    }

    suspend fun insert(article: Article): Long = dao.insert(article)

    fun getAllArticles(): Flow<List<Article>> = dao.getAllArticles()

    suspend fun deleteArticle(article: Article) = dao.deleteArticle(article)

}