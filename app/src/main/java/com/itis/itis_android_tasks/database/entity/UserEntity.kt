package com.itis.itis_android_tasks.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user", indices = [Index(value = ["phone_number"], unique = true), Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @ColumnInfo(name = "user_id")
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "email")
    val email: String,
    val password: String,
    @ColumnInfo(name = "delete_date")
    val deleteDate: String?
)
