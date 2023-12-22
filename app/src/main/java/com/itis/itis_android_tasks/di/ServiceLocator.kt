package com.itis.itis_android_tasks.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.itis.itis_android_tasks.database.LibraryDatabase

object ServiceLocator {
    private var dbInstance: LibraryDatabase? = null

    private var pref: SharedPreferences? = null

    fun initDatabase(ctx: Context) {
        dbInstance = Room.databaseBuilder(ctx, LibraryDatabase::class.java, "library.db").build()

        pref = ctx.getSharedPreferences("library_pref", Context.MODE_PRIVATE)

    }

    fun getDbInstance(): LibraryDatabase {
        return dbInstance ?: throw IllegalStateException("DB has not been initialized.")
    }

    fun getSharedPreferences(): SharedPreferences {
        return pref ?: throw IllegalStateException("Preferences have not been initialized.")
    }
}
