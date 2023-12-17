package com.itis.itis_android_tasks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.itis_android_tasks.database.entity.RatingEntity

@Dao
interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRating(ratingEntity: RatingEntity)

    @Query("SELECT * FROM rating WHERE user_id = :userId AND book_id = :bookId")
    fun getRatingByUserAndBookIds(userId: String, bookId: String): RatingEntity?

    @Query("SELECT * FROM rating WHERE book_id = :bookId")
    fun getAllBookRatings(bookId: String): List<RatingEntity>?

//    @Query("UPDATE rating SET rating = :rating WHERE user_id = :userId AND book_id = :bookId")
//    fun updateUserPassword(userId: String, bookId: String, rating: Double)
}
