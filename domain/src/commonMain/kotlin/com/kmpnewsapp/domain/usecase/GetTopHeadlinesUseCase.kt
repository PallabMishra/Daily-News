package com.kmpnewsapp.domain.usecase

import com.kmpnewsapp.domain.model.Article
import com.kmpnewsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetTopHeadlinesUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<List<Article>> {
        return repository.getTopHeadlines(forceRefresh)
    }
}
