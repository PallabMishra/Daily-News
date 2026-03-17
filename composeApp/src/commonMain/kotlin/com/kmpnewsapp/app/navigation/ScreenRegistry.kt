package com.kmpnewsapp.app.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.kmpnewsapp.core.utils.navigation.SharedScreen
import com.kmpnewsapp.feature.auth.LoginScreen
import com.kmpnewsapp.feature.dashboard.DashboardScreen
import com.kmpnewsapp.feature.map.MapScreen
import com.kmpnewsapp.feature.news.ui.NewsListScreen
import com.kmpnewsapp.feature.splash.SplashScreen

fun registerScreens() {
    ScreenRegistry {
        register<SharedScreen.Splash> { SplashScreen() }
        register<SharedScreen.Auth> { LoginScreen() }
        register<SharedScreen.Dashboard> { DashboardScreen() }
        register<SharedScreen.News> { NewsListScreen() }
        register<SharedScreen.Map> { MapScreen() }
    }
}
