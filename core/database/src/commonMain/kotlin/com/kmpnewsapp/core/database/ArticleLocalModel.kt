package com.kmpnewsapp.core.database

data class ArticleLocalModel(
    val url: String,
    val author: String?,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val content: String?
)
