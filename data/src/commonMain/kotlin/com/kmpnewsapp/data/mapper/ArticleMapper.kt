package com.kmpnewsapp.data.mapper

import com.kmpnewsapp.data.repository.ArticleLocalModel
import com.kmpnewsapp.domain.model.Article
fun ArticleLocalModel.toDomain(): Article {
    return Article(
        id = this.url,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        imageUrl = this.imageUrl,
        publishedAt = this.publishedAt,
        content = this.content
    )
}
