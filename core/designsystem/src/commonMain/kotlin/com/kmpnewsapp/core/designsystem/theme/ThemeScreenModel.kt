package com.kmpnewsapp.core.designsystem.theme

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kmpnewsapp.core.database.NewsDao
import com.kmpnewsapp.core.database.SettingEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeScreenModel(private val newsDao: NewsDao) : ScreenModel {
    val isDarkTheme: StateFlow<Boolean> = newsDao.getSettings()
        .map { it?.isDarkTheme ?: false }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleTheme() {
        screenModelScope.launch {
            val current = isDarkTheme.value
            newsDao.saveSettings(SettingEntity(isDarkTheme = !current))
        }
    }
}
