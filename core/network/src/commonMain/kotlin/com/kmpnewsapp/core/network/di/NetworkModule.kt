package com.kmpnewsapp.core.network.di

import com.kmpnewsapp.core.network.NewsApiClient
import com.kmpnewsapp.core.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { NewsApiClient(get()) }
}
