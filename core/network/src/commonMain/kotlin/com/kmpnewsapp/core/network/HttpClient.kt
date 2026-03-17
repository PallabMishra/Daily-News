package com.kmpnewsapp.core.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
