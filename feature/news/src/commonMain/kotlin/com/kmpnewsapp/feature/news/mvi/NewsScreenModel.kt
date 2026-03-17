package com.kmpnewsapp.feature.news.mvi

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kmpnewsapp.domain.usecase.GetTopHeadlinesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsScreenModel(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(NewsState())
    val state: StateFlow<NewsState> = _state.asStateFlow()

    init {
        processIntent(NewsIntent.LoadNews)
    }

    fun processIntent(intent: NewsIntent) {
        when (intent) {
            is NewsIntent.LoadNews -> loadNews(forceRefresh = false)
            is NewsIntent.RefreshNews -> loadNews(forceRefresh = true)
        }
    }

    private fun loadNews(forceRefresh: Boolean) {
        screenModelScope.launch {
            getTopHeadlinesUseCase(forceRefresh)
                .onStart {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                }
                .catch { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
                .collect { articles ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        articles = articles,
                        error = null
                    )
                }
        }
    }
}
