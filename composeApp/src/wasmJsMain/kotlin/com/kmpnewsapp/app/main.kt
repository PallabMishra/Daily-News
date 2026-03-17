package com.kmpnewsapp.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kmpnewsapp.app.di.initKoin
import com.kmpnewsapp.app.navigation.registerScreens
import kotlinx.browser.document
import com.kmpnewsapp.app.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    registerScreens()
    ComposeViewport(document.body!!) {
        App()
    }
}
