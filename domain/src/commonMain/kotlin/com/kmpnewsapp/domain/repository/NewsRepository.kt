package com.kmpnewsapp.domain.repository

import com.kmpnewsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(forceRefresh: Boolean): Flow<List<Article>>
    suspend fun syncHeadlines()
    suspend fun clearCache()
}
