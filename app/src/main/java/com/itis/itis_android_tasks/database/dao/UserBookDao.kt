package com.itis.itis_android_tasks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.itis.itis_android_tasks.database.entity.BookEntity
import com.itis.itis_android_tasks.database.entity.UserBookEntity
import com.itis.itis_android_tasks.database.entity.UserEntity
import com.itis.itis_android_tasks.database.entity.UserFavoriteBooks

@Dao
interface UserBookDao {
    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUserFavoriteBooks(userId: String): UserFavoriteBooks

    @Query("DELETE FROM user_book WHERE user_id = :userId AND book_id = :bookId")
    fun deleteUserBookByIds(userId: String, bookId: String)

    @Query("DELETE FROM user_book WHERE user_id = :userId")
    fun deleteAllUserFavorites(userId: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addUserBook(userBookEntity: UserBookEntity)
}
