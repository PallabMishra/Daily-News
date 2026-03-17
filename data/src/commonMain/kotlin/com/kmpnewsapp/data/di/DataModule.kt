package com.kmpnewsapp.data.di

import com.kmpnewsapp.data.repository.NewsRepositoryImpl
import com.kmpnewsapp.domain.repository.NewsRepository
import com.kmpnewsapp.domain.usecase.GetTopHeadlinesUseCase
import org.koin.dsl.module
import org.koin.core.module.Module

expect fun localDataModule(): Module

val dataModule = module {
    includes(localDataModule())
    single<NewsRepository> { NewsRepositoryImpl(apiClient = get(), localDataSource = get()) }
    factory { GetTopHeadlinesUseCase(get()) }
}

