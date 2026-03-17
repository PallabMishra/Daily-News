package com.kmpnewsapp.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val fullName: String,
    val password: String,
    val profileImage: ByteArray? = null
)
