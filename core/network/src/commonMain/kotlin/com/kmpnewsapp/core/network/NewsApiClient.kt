package com.kmpnewsapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val BASE_URL = "https://newsapi.org/v2"
private const val API_KEY = "ce680d1613954b10a4929b774e3f718a"

class NewsApiClient(
    private val httpClient: HttpClient
) {
    suspend fun getTopHeadlines(country: String = "us"): String {
        return httpClient.get("$BASE_URL/top-headlines") {
            contentType(ContentType.Application.Json)
            parameter("country", country)
            parameter("apiKey", API_KEY)
        }.bodyAsText()
    }

    suspend fun getEverything(
        query: String? = null,
        sources: String? = null,
        domains: String? = null,
        from: String? = null,
        to: String? = null,
        language: String? = null,
        sortBy: String? = "publishedAt",
        pageSize: Int? = null,
        page: Int? = null
    ): String {
        return httpClient.get("$BASE_URL/everything") {
            contentType(ContentType.Application.Json)
            query?.let { parameter("q", it) }
            sources?.let { parameter("sources", it) }
            domains?.let { parameter("domains", it) }
            from?.let { parameter("from", it) }
            to?.let { parameter("to", it) }
            language?.let { parameter("language", it) }
            sortBy?.let { parameter("sortBy", it) }
            pageSize?.let { parameter("pageSize", it) }
            page?.let { parameter("page", it) }
            parameter("apiKey", API_KEY)
        }.bodyAsText()
    }
}
