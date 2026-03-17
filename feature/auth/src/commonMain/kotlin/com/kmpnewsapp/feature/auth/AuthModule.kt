package com.kmpnewsapp.feature.auth

import org.koin.dsl.module

val authModule = module {
    factory { AuthViewModel(get()) }
}
