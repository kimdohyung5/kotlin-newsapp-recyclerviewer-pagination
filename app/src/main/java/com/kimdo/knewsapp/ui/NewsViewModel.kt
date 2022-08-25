package com.kimdo.knewsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimdo.knewsapp.models.Article
import com.kimdo.knewsapp.models.NewsResponse
import com.kimdo.knewsapp.repository.NewsRepository
import com.kimdo.knewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _breakinghNewsStateFlow = MutableStateFlow<Resource<NewsResponse>>( Resource.Loading() )
    val breakingNewsStateFlow: StateFlow<Resource<NewsResponse>> = _breakinghNewsStateFlow.asStateFlow()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    private val _searchNewsStateFlow = MutableStateFlow<Resource<NewsResponse>>( Resource.Loading() )
    val searchNewsStateFlow: StateFlow<Resource<NewsResponse>> = _searchNewsStateFlow.asStateFlow()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            _breakinghNewsStateFlow.value = Resource.Loading()
            val response = repository.getBreakingNews(countryCode, breakingNewsPage )
            if( response is Resource.Success) {
                breakingNewsPage++
                if(breakingNewsResponse == null) {
                    breakingNewsResponse = response.data
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = response.data?.articles!!
                    oldArticles?.addAll(newArticles)
                    breakingNewsResponse = breakingNewsResponse!!.copy(status = response.data.status, totalResults = response.data.totalResults)
                }
                _breakinghNewsStateFlow.value = Resource.Success( data = breakingNewsResponse )
            } else {
                _breakinghNewsStateFlow.value = response
            }

        }
    }

    private var job: Job? = null
    fun getSearchNews(searchQuery: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _searchNewsStateFlow.value = Resource.Loading()
            delay(500)

            val response = repository.searchForNews(searchQuery, searchNewsPage )
            if( response is Resource.Success) {
                searchNewsPage++
                if(searchNewsResponse == null) {
                    searchNewsResponse = response.data
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = response.data?.articles!!
                    oldArticles?.addAll(newArticles)
                    searchNewsResponse = searchNewsResponse!!.copy(status = response.data.status, totalResults = response.data.totalResults)
                }
                _searchNewsStateFlow.value = Resource.Success( data = searchNewsResponse )
            } else {
                _searchNewsStateFlow.value = response
            }

        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun getSavedNews() = repository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

}