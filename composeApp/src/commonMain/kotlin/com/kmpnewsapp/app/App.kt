package com.kmpnewsapp.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kmpnewsapp.core.designsystem.theme.KMPNewsTheme
import com.kmpnewsapp.core.designsystem.theme.ThemeScreenModel
import com.kmpnewsapp.feature.splash.SplashScreen
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.kmpnewsapp.core.utils.configurePlatform
import org.koin.compose.koinInject
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .configurePlatform()
            .build()
    }
    
    val themeScreenModel = koinInject<ThemeScreenModel>()
    val isDarkTheme by themeScreenModel.isDarkTheme.collectAsState()

    KMPNewsTheme(useDarkTheme = isDarkTheme) {
        Navigator(SplashScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}
