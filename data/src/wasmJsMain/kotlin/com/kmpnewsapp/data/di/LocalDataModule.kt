package com.kmpnewsapp.data.di

import com.kmpnewsapp.data.repository.InMemoryNewsDataSource
import com.kmpnewsapp.data.repository.NewsLocalDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun localDataModule(): Module = module {
    single<NewsLocalDataSource> { InMemoryNewsDataSource() }
}
