package com.kmpnewsapp.data.repository

import com.kmpnewsapp.core.network.NewsApiClient
import com.kmpnewsapp.data.mapper.toDomain
import com.kmpnewsapp.domain.model.Article
import com.kmpnewsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.emitAll
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive


class NewsRepositoryImpl(
    private val apiClient: NewsApiClient,
    private val localDataSource: NewsLocalDataSource
) : NewsRepository {

    override fun getTopHeadlines(forceRefresh: Boolean): Flow<List<Article>> = flow {
        val cached = localDataSource.getAllArticles().first()
        if (cached.isNotEmpty() && !forceRefresh) {
            emit(cached.map { it.toDomain() })
            return@flow
        }
        
        syncHeadlines()
        emitAll(localDataSource.getAllArticles().map { models -> models.map { it.toDomain() } })
    }

    override suspend fun syncHeadlines() {
        try {
            val responseText = apiClient.getTopHeadlines()
            val json = Json { ignoreUnknownKeys = true }.parseToJsonElement(responseText).jsonObject
            val articlesArray = json["articles"]?.jsonArray ?: emptyList()
            
            val localModels = articlesArray.map { element ->
                val obj = element.jsonObject
                ArticleLocalModel(
                    url = obj["url"]?.jsonPrimitive?.content ?: "",
                    author = obj["author"]?.jsonPrimitive?.content,
                    title = obj["title"]?.jsonPrimitive?.content ?: "",
                    description = obj["description"]?.jsonPrimitive?.content,
                    imageUrl = obj["urlToImage"]?.jsonPrimitive?.content,
                    publishedAt = obj["publishedAt"]?.jsonPrimitive?.content ?: "",
                    content = obj["content"]?.jsonPrimitive?.content
                )
            }
            
            localDataSource.clearAll()
            localDataSource.insertArticles(localModels)
        } catch (e: Exception) {
            // Rethrow or handle as needed. SyncWorker will retry on exception.
            throw e
        }
    }

    override suspend fun clearCache() {
        localDataSource.clearAll()
    }
}
