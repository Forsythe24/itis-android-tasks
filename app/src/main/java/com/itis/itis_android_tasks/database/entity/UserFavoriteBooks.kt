package com.itis.itis_android_tasks.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserFavoriteBooks (
    @Embedded val userEntity: UserEntity,
    @Relation(
         parentColumn = "user_id",
         entityColumn = "book_id",
         associateBy = Junction(UserBookEntity::class)
    )
    val books: List<BookEntity>
)

