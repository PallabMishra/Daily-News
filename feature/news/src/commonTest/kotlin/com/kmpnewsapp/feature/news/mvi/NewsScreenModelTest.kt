package com.kmpnewsapp.feature.news.mvi

import com.kmpnewsapp.domain.model.Article
import com.kmpnewsapp.domain.repository.NewsRepository
import com.kmpnewsapp.domain.usecase.GetTopHeadlinesUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NewsScreenModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: NewsRepository
    private lateinit var getTopHeadlinesUseCase: GetTopHeadlinesUseCase
    private lateinit var screenModel: NewsScreenModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        getTopHeadlinesUseCase = GetTopHeadlinesUseCase(repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadNews intent should update state with articles from repository`() = runTest {
        // Given
        val articles = listOf(
            Article(
                id = "1",
                author = "Test Author",
                title = "Test Title",
                description = "Test Desc",
                url = "https://test.com",
                imageUrl = null,
                publishedAt = "2024-03-18",
                content = null
            )
        )
        every { repository.getTopHeadlines(false) } returns flowOf(articles)

        // When
        screenModel = NewsScreenModel(getTopHeadlinesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = screenModel.state.value
        assertEquals(articles, state.articles)
        assertEquals(false, state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `RefreshNews intent should call repository with forceRefresh true`() = runTest {
        // Given
        val articles = listOf(
            Article("1", null, "Refreshed Title", null, "url", null, "date", null)
        )
        // LoadNews is called in init
        every { repository.getTopHeadlines(false) } returns flowOf(emptyList())
        every { repository.getTopHeadlines(true) } returns flowOf(articles)

        screenModel = NewsScreenModel(getTopHeadlinesUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        screenModel.processIntent(NewsIntent.RefreshNews)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = screenModel.state.value
        assertEquals(articles, state.articles)
        assertEquals(false, state.isLoading)
    }
}
