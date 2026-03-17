package com.kmpnewsapp.app.di

import com.kmpnewsapp.core.database.di.databaseModule
import com.kmpnewsapp.core.network.di.networkModule
import com.kmpnewsapp.data.di.dataModule
import com.kmpnewsapp.feature.auth.authModule
import com.kmpnewsapp.feature.map.MapScreenModel
import com.kmpnewsapp.feature.news.mvi.NewsScreenModel
import com.kmpnewsapp.core.designsystem.theme.ThemeScreenModel
import org.koin.dsl.module

val featureModule = module {
    factory { NewsScreenModel(get()) }
    factory { MapScreenModel(get()) }
    factory { ThemeScreenModel(get()) }
}

fun getAppModules(context: Any? = null) = listOf(
    networkModule,
    databaseModule(context),
    dataModule,
    featureModule,
    authModule
)
