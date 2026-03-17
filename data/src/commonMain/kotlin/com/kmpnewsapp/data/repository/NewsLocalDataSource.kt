package com.kmpnewsapp.data.repository

import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getAllArticles(): Flow<List<ArticleLocalModel>>
    suspend fun insertArticles(articles: List<ArticleLocalModel>)
    suspend fun clearAll()
}
