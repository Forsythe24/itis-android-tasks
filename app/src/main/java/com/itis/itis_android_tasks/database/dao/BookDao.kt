package com.itis.itis_android_tasks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.itis_android_tasks.database.entity.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addBook(bookEntity: BookEntity)

    @Query("SELECT * FROM book")
    fun getAllBooks(): List<BookEntity>?

    @Query("SELECT * FROM book WHERE book_id = :id")
    fun getBookById(id: String): BookEntity?

    @Query("DELETE FROM book WHERE book_id = :id")
    fun deleteBookById(id: String)

    @Query("UPDATE book SET rating = :rating WHERE book_id = :id")
    fun updateBookRating(id: String, rating: Double)
}
