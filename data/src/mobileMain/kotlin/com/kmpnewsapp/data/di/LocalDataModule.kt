package com.kmpnewsapp.data.di

import com.kmpnewsapp.data.repository.NewsLocalDataSource
import com.kmpnewsapp.data.repository.RoomNewsDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun localDataModule(): Module = module {
    single<NewsLocalDataSource> { RoomNewsDataSource(appDatabase = get()) }
}
