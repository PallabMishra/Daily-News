package com.kmpnewsapp.feature.news.mvi

import com.kmpnewsapp.domain.model.Article

data class NewsState(
    val isLoading: Boolean = true,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)

sealed class NewsIntent {
    object LoadNews : NewsIntent()
    object RefreshNews : NewsIntent()
}

sealed class NewsSideEffect {
    data class ShowError(val message: String) : NewsSideEffect()
}
