package com.kmpnewsapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryNewsDataSource : NewsLocalDataSource {
    private val _articles = MutableStateFlow<List<ArticleLocalModel>>(emptyList())

    override fun getAllArticles(): Flow<List<ArticleLocalModel>> {
        return _articles.asStateFlow()
    }

    override suspend fun insertArticles(articles: List<ArticleLocalModel>) {
        _articles.update { articles } // Simple exact replacement to match existing caching logic
    }

    override suspend fun clearAll() {
        _articles.update { emptyList() }
    }
}
