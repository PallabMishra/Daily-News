package com.kmpnewsapp

import com.kmpnewsapp.app.di.getAppModules
import com.kmpnewsapp.app.navigation.registerScreens
import org.koin.core.context.startKoin

fun initKoin() {
    registerScreens()
    startKoin {
        modules(getAppModules())
    }
}
