package com.itis.itis_android_tasks.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "book", indices = [Index(value = ["title", "publication_year"], unique = true)])
data class BookEntity (
    @ColumnInfo(name = "book_id")
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    @ColumnInfo(name = "publication_year")
    val publicationYear: Int,
    val rating: Double
)
