package com.kmpnewsapp.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kmpnewsapp.core.designsystem.components.NewsCardLarge
import com.kmpnewsapp.feature.news.ui.ArticleDetailScreen
import com.swmansion.kmpmaps.core.CameraPosition
import com.swmansion.kmpmaps.core.Coordinates
import com.swmansion.kmpmaps.core.MapProperties
import com.swmansion.kmpmaps.core.MapType
import com.swmansion.kmpmaps.core.MapUISettings
import com.swmansion.kmpmaps.core.Marker
import com.swmansion.kmpmaps.googlemaps.Map

class MapScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainNavigator = navigator.parent ?: navigator
        
        val screenModel = getScreenModel<MapScreenModel>()
        val state by screenModel.state.collectAsState()
        
        val cameraPosition = remember(state.currentLocation) {
            CameraPosition(
                coordinates = state.currentLocation ?: Coordinates(20.5937, 78.9629),
                zoom = 5f
            )
        }

        val mapProperties = remember {
            MapProperties(mapType = MapType.NORMAL)
        }

        val uiSettings = remember {
            MapUISettings(compassEnabled = true)
        }

        val markers = remember(state.currentLocation) {
            val list = mutableListOf<Marker>()
            state.currentLocation?.let {
                list.add(Marker(coordinates = it, title = "Your Location"))
            }
            list.addAll(getCityMarkers())
            list
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Map(
                modifier = Modifier.fillMaxSize(),
                cameraPosition = cameraPosition,
                properties = mapProperties,
                uiSettings = uiSettings,
                markers = markers,
                onMarkerClick = { marker ->
                    val city = INDIAN_CITIES.find { it.name == marker.title }
                    if (city != null) {
                        screenModel.selectDestination(city)
                    }
                }
            )

            // News Slider at the Bottom
            if (state.articles.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    val pagerState = rememberPagerState(pageCount = { state.articles.size })
                    
                    Text(
                        text = "News in ${state.selectedCity?.name ?: ""}",
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        color = MaterialTheme.colors.onSurface
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        pageSpacing = 16.dp
                    ) { page ->
                        val article = state.articles[page]
                        NewsCardLarge(
                            title = article.title,
                            description = article.description,
                            author = article.author,
                            imageUrl = article.imageUrl,
                            onClick = { mainNavigator.push(ArticleDetailScreen(article)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Pager Indicators
                    Row(
                        Modifier
                            .height(24.dp)
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(state.articles.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) 
                                MaterialTheme.colors.primary 
                            else 
                                MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }
            
            if (state.isLoadingNews) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}
