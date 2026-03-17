package com.kmpnewsapp.core.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kmpnewsapp.core.database.AppDatabase
import com.kmpnewsapp.core.database.getDatabaseBuilder
import org.koin.dsl.module

fun databaseModule(context: Any? = null) = module {
    single<AppDatabase> {
        getDatabaseBuilder(context)
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<AppDatabase>().newsDao() }
}
