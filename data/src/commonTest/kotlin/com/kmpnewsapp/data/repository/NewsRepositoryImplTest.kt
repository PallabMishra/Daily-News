package com.kmpnewsapp.data.repository

import com.kmpnewsapp.core.network.NewsApiClient
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsRepositoryImplTest {

    private lateinit var apiClient: NewsApiClient
    private lateinit var localDataSource: NewsLocalDataSource
    private lateinit var repository: NewsRepositoryImpl

    @BeforeTest
    fun setup() {
        apiClient = mock()
        localDataSource = mock()
        repository = NewsRepositoryImpl(apiClient, localDataSource)
    }

    @Test
    fun `getTopHeadlines should return cached articles when forceRefresh is false and cache is not empty`() = runTest {
        // Given
        val cachedArticles = listOf(
            ArticleLocalModel(
                url = "https://test.com",
                author = "Author",
                title = "Cached Title",
                description = "Desc",
                imageUrl = null,
                publishedAt = "2024-03-18",
                content = null
            )
        )
        every { localDataSource.getAllArticles() } returns flowOf(cachedArticles)

        // When
        val result = repository.getTopHeadlines(forceRefresh = false).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Cached Title", result[0].title)
        verify { localDataSource.getAllArticles() }
    }

    @Test
    fun `clearCache should call localDataSource clearAll`() = runTest {
        // When
        repository.clearCache()

        // Then
        verify { localDataSource.clearAll() }
    }
}
