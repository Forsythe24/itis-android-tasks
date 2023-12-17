package com.itis.itis_android_tasks.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "rating", indices = [Index(value = ["book_id", "user_id"], unique = true)])
data class RatingEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "book_id")
    val bookId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    val rating: Double
)
