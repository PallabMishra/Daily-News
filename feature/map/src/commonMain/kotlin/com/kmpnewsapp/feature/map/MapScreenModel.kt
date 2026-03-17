package com.kmpnewsapp.feature.map

import cafe.adriel.voyager.core.model.ScreenModel
import com.swmansion.kmpmaps.core.Coordinates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.kmpnewsapp.domain.repository.NewsRepository
import com.kmpnewsapp.domain.model.Article
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import cafe.adriel.voyager.core.model.screenModelScope

data class MapState(
    val currentLocation: Coordinates? = Coordinates(28.6139, 77.2090), // Default to New Delhi
    val selectedCity: IndianCity? = null,
    val articles: List<Article> = emptyList(),
    val isLoadingNews: Boolean = false
)

class MapScreenModel(
    private val newsRepository: NewsRepository
) : ScreenModel {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    fun selectDestination(city: IndianCity) {
        _state.value = _state.value.copy(
            selectedCity = city,
            isLoadingNews = true
        )
        fetchNewsForCity()
    }

    private fun fetchNewsForCity() {
        screenModelScope.launch {
            try {
                // For now, we fetch top headlines as a proxy for city news 
                // since Article doesn't have city metadata yet.
                val allArticles = newsRepository.getTopHeadlines(false).first()
                val cityArticles = allArticles.shuffled().take(5) // Just taking 5 to simulate city news
                
                _state.value = _state.value.copy(
                    articles = cityArticles,
                    isLoadingNews = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoadingNews = false,
                    articles = emptyList()
                )
            }
        }
    }

    fun updateCurrentLocation(lat: Double, lng: Double) {
        _state.value = _state.value.copy(
            currentLocation = Coordinates(lat, lng)
        )
    }
}
