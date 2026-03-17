package com.kmpnewsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class, UserEntity::class, SettingEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
