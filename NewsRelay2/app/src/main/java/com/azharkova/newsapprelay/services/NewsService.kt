package com.azharkova.newsapprelay.services

import com.azharkova.newsapprelay.data.NewsList

class NewsService {
    private val apiService by lazy {
        ApiService.getInstance()
    }

    suspend fun loadNews(): NewsList {
       return apiService.loadNews()
    }
}