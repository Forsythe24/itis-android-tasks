package com.itis.itis_android_tasks.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_book", primaryKeys = ["user_id", "book_id"])
data class UserBookEntity (
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "book_id")
    val bookId: String
    )
