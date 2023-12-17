package com.itis.itis_android_tasks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.itis_android_tasks.database.dao.BookDao
import com.itis.itis_android_tasks.database.dao.RatingDao
import com.itis.itis_android_tasks.database.dao.UserBookDao
import com.itis.itis_android_tasks.database.dao.UserDao
import com.itis.itis_android_tasks.database.entity.BookEntity
import com.itis.itis_android_tasks.database.entity.RatingEntity
import com.itis.itis_android_tasks.database.entity.UserBookEntity
import com.itis.itis_android_tasks.database.entity.UserEntity


@Database(entities = [UserEntity::class, BookEntity::class, RatingEntity::class, UserBookEntity::class], version = 1)
abstract class LibraryDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val bookDao: BookDao
    abstract val userBookDao: UserBookDao
    abstract val ratingDao: RatingDao
}
