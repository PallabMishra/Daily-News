package com.kmpnewsapp.core.utils.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    data object Splash : SharedScreen()
    data object Auth : SharedScreen()
    object Dashboard : SharedScreen()
    object News : SharedScreen()
    object Map : SharedScreen()
}
