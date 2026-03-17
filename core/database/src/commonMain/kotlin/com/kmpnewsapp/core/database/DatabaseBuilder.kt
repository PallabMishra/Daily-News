package com.kmpnewsapp.core.database

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(context: Any?): RoomDatabase.Builder<AppDatabase>
