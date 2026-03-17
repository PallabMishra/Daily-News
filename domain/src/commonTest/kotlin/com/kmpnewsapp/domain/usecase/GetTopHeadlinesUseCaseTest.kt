package com.kmpnewsapp.domain.usecase

import com.kmpnewsapp.domain.model.Article
import com.kmpnewsapp.domain.repository.NewsRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTopHeadlinesUseCaseTest {

    private lateinit var repository: NewsRepository
    private lateinit var useCase: GetTopHeadlinesUseCase

    @BeforeTest
    fun setup() {
        repository = mock()
        useCase = GetTopHeadlinesUseCase(repository)
    }

    @Test
    fun `invoke should call repository getTopHeadlines`() = runTest {
        // Given
        val articles = listOf(
            Article(
                id = "1",
                author = "Author",
                title = "Title",
                description = "Description",
                url = "url",
                imageUrl = "image",
                publishedAt = "2024-03-10",
                content = "content"
            )
        )
        every { repository.getTopHeadlines(any()) } returns flowOf(articles)

        // When
        val result = useCase.invoke(forceRefresh = true)

        // Then
        result.collect {
            assertEquals(articles, it)
        }
        verify { repository.getTopHeadlines(true) }
    }
}
