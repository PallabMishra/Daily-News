package com.kmpnewsapp.data.repository

import com.kmpnewsapp.core.database.AppDatabase
import com.kmpnewsapp.core.database.ArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomNewsDataSource(
    private val appDatabase: AppDatabase
) : NewsLocalDataSource {
    override fun getAllArticles(): Flow<List<ArticleLocalModel>> {
        return appDatabase.newsDao().getAllArticles().map { entities ->
            entities.map {
                ArticleLocalModel(
                    url = it.url,
                    author = it.author,
                    title = it.title,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    publishedAt = it.publishedAt,
                    content = it.content
                )
            }
        }
    }

    override suspend fun insertArticles(articles: List<ArticleLocalModel>) {
        appDatabase.newsDao().insertArticles(
            articles.map {
                ArticleEntity(
                    url = it.url,
                    author = it.author,
                    title = it.title,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    publishedAt = it.publishedAt,
                    content = it.content
                )
            }
        )
    }

    override suspend fun clearAll() {
        appDatabase.newsDao().clearAll()
    }
}
