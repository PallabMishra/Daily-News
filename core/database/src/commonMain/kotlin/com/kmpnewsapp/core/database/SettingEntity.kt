package com.kmpnewsapp.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val id: String = "app_settings",
    val isDarkTheme: Boolean = false
)
