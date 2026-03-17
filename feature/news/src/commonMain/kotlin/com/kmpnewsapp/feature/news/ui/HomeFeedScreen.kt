@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.kmpnewsapp.feature.news.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.kmpnewsapp.core.designsystem.components.CategoryChip
import com.kmpnewsapp.core.designsystem.components.NewsCardLarge
import com.kmpnewsapp.core.designsystem.components.NewsCardHorizontal
import com.kmpnewsapp.core.designsystem.theme.BackgroundLight
import com.kmpnewsapp.core.designsystem.theme.PrimaryBlue
import com.kmpnewsapp.core.designsystem.theme.TextPrimary
import com.kmpnewsapp.feature.news.mvi.NewsScreenModel

class HomeFeedScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // Use the parent navigator to push screens over the TabNavigator
        val mainNavigator = navigator.parent ?: navigator
        
        val screenModel = getScreenModel<NewsScreenModel>()
        val state by screenModel.state.collectAsState()
        var selectedCategory by remember { mutableStateOf("All") }
        val categories = listOf("All", "Politics", "Tech", "Health", "Science", "Sports")

        Scaffold(
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    title = {
                        Text(
                            text = "Daily News",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.primary
                        )
                    }
                )
            },
            backgroundColor = MaterialTheme.colors.background
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colors.primary)
                } else if (state.error != null) {
                    Text(text = state.error!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        // Categories Section
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                            ) {
                                items(categories) { category ->
                                    CategoryChip(
                                        text = category,
                                        isSelected = selectedCategory == category,
                                        onClick = { selectedCategory = category }
                                    )
                                }
                            }
                        }

                        // Carousel Section (Top 5 Headlines)
                        if (state.articles.isNotEmpty()) {
                            val carouselArticles = state.articles.take(5)
                            item {
                                Text(
                                    text = "Top Headlines",
                                    style = MaterialTheme.typography.h3,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                val pagerState = rememberPagerState(pageCount = { carouselArticles.size })
                                
                                Column {
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxWidth()
                                    ) { page ->
                                        val article = carouselArticles[page]
                                        NewsCardLarge(
                                            title = article.title,
                                            description = article.description,
                                            author = article.author,
                                            imageUrl = article.imageUrl,
                                            onClick = { mainNavigator.push(ArticleDetailScreen(article)) },
                                            modifier = Modifier.padding(horizontal = 4.dp)
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
                                        repeat(carouselArticles.size) { iteration ->
                                            val color = if (pagerState.currentPage == iteration) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
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
                                
                                Spacer(modifier = Modifier.height(32.dp))
                            }

                            // Latest News Header
                            item {
                                Text(
                                    text = "Latest News",
                                    style = MaterialTheme.typography.h3,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            // List of Articles (Remaining)
                            val remainingArticles = if (state.articles.size > 5) state.articles.drop(5) else emptyList()
                            items(remainingArticles) { article ->
                                NewsCardHorizontal(
                                    title = article.title,
                                    time = article.publishedAt.take(10),
                                    imageUrl = article.imageUrl,
                                    onClick = { mainNavigator.push(ArticleDetailScreen(article)) }
                                )
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                            }
                        }
                    }
                }
            }
        }
    }
}
