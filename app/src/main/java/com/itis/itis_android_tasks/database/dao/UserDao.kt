package com.itis.itis_android_tasks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itis.itis_android_tasks.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addUser(userEntity: UserEntity)

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<UserEntity>?

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE user SET phone_number = :phoneNumber WHERE user_id = :id")
    fun updateUserPhoneNumber(id: String, phoneNumber: String)

    @Query("UPDATE user SET delete_date = :deleteDate WHERE user_id = :id")
    fun updateUserDeleteDate(id: String, deleteDate: String?)

    @Query("UPDATE user SET password = :password WHERE user_id = :id")
    fun updateUserPassword(id: String, password: String)

    @Query("DELETE FROM user WHERE user_id = :id")
    fun deleteUserById(id: String)
}
