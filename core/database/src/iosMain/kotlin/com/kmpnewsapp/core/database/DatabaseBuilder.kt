package com.kmpnewsapp.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/news.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
        factory = { AppDatabase::class.instantiateImpl() }
    )
}
